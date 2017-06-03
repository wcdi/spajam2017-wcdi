package tech.wcdi.spajam.myapplication.Graphics.Rect;

/**
 * Created by G015C1143 on 2017/06/03.
 */

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Date;

import tech.wcdi.spajam.myapplication.Graphics.OGLRenderer;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Square {

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {-0.05f, 0.0f, 1.0f,   // top left
                                   0.0f, -0.05f, 1.0f,   // bottom left
                                   0.05f, 0.0f, 1.0f,   // bottom right
                                   0.0f, 0.05f, 1.0f}; // top right
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;" + "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" + "}";
    private final String
            fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private final short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    float[] rotate = new float[16];
    double degree = 0.0;
    double higher = 0.0;
    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    float[] mvpMatrix = new float[12];
    private float color[] = {0.2f, 0.709803922f, 0.898039216f, 1.0f};
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Square(double a, double h) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = OGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = OGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        degree = a;
        higher = h;

        mProgram = GLES20.glCreateProgram();
        // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
    }

    static double calcMjd(double year, double month, double day) {
        double y, m;
        if (month <= 2) {
            y = year - 1.0;
            m = month + 12.0;
        } else {
            y = year;
            m = month;
        }
        return Math.round((Math.floor(365.25 * y) + Math.floor(y / 400.0) - Math.floor(y / 100.0)) +
                Math.floor(30.59 * (m - 2.0)) +
                day - 678912.0);
    }

    static double[] ec2hc(float latitude, float longitude, int alphadec, int deltadec) {

        Date date = new Date();
        double year = date.getYear();
        double month = date.getMonth();
        double day = date.getDay();
        double hour = date.getHours();
        double minute = date.getMinutes();
        double second = date.getSeconds();
        double alpha = (double) alphadec / 3600.0;
        double delta = (double) deltadec / 3600.0;
        double PI = 3.14159265358979;
        double RAD = 180.0 / PI;

        double
                mjd =
                calcMjd(year, month, day) + hour / 24.0 + minute / 1440.0 + second / 86400.0 -
                        0.375;
        double d = (0.671262 + 1.002737909 * (mjd - 40000.0) + longitude / 360.0);
        double lst = 2.0 * PI * (d - Math.floor(d));

        double srid = Math.sin(latitude / RAD);
        double crid = Math.cos(latitude / RAD);
        double ra = 15.0 * alpha / RAD;
        double dc = delta / RAD;
        double ha = lst - ra;
        double sdc = Math.sin(dc);
        double cdc = Math.cos(dc);
        double sha = Math.sin(ha);
        double cha = Math.cos(ha);
        double xs = sdc * srid + cdc * crid * cha;
        double h = Math.asin(xs);
        double s = cdc * sha;
        double c = cdc * srid * cha - sdc * crid;
        double a;
        if (c < 0) {
            a = Math.atan(s / c) + PI;
        } else if (c > 0 && s <= 0) {
            a = Math.atan(s / c) + 2 * PI;
        } else {
            a = Math.atan(s / c);
        }
        if (h == 0) {
            h = 0.00001;
        }
        a = a * RAD;
        h = h * RAD;
        double rt = Math.tan((h + 8.6 / (h + 4.4)) / RAD);
        h = h + 0.0167 / rt;

        return new double[]{(a + 180.0) % 360.0, h};
    }

    public void draw(float[] mMvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        OGLRenderer.checkGlError("glGetUniformLocation");
        Matrix.setRotateM(rotate, 0, (float) higher, (float) degree, 0, 1.0f);
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mMvpMatrix, 0, rotate, 0);

        OGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}