package tech.wcdi.spajam.myapplication.Graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by G015C1143 on 2017/06/03.
 */

public class OGLSurfaceView extends GLSurfaceView {

    public OGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(new OGLRenderer());

    }
}