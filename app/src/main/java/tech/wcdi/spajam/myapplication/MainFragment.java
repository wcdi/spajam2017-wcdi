package tech.wcdi.spajam.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import java.util.List;

public class MainFragment extends Fragment implements SensorEventListener {

    private static final int MATRIX_SIZE = 16;
    SensorManager mSensorManager;
    boolean mIsMagSensor = false, mIsAccSensor = false;
    /* 回転行列 */ float[] inR = new float[MATRIX_SIZE];
    float[] outR = new float[MATRIX_SIZE];
    float[] I = new float[MATRIX_SIZE];
    /* センサーの値 */ float[] orientationValues = new float[3];
    float[] magneticValues = new float[3];
    float[] accelerometerValues = new float[3];
    XWalkView xWalkView;
    private GLSurfaceView mGLView;

    public MainFragment() {
        super();
        mSensorManager = MainActivity.mSensorManager;
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        /*
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        */

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        xWalkView = (XWalkView) view.findViewById(R.id.xWalkView);
        xWalkView.setResourceClient(new XWalkResourceClient(xWalkView) {
            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                return false;
            }
        });
        xWalkView.addJavascriptInterface(new DataTransfer(), "dataTransfer");
        xWalkView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onPause() {
        super.onPause();

        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }

        //センサーマネージャのリスナ登録破棄
        if (mIsMagSensor || mIsAccSensor) {
            mSensorManager.unregisterListener(this);
            mIsMagSensor = false;
            mIsAccSensor = false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }

        // センサの取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサマネージャへリスナーを登録(implements SensorEventListenerにより、thisで登録する)
        for (Sensor sensor : sensors) {

            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mSensorManager.registerListener((SensorEventListener) this,
                        sensor,
                        SensorManager.SENSOR_DELAY_GAME);
                mIsMagSensor = true;
            }

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mSensorManager.registerListener((SensorEventListener) this,
                        sensor,
                        SensorManager.SENSOR_DELAY_GAME);
                mIsAccSensor = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (xWalkView != null)
            xWalkView.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticValues = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values.clone();
                break;
        }

        if (magneticValues != null && accelerometerValues != null) {

            SensorManager.getRotationMatrix(inR, I, accelerometerValues, magneticValues);

            //Activityの表示が縦固定の場合。横向きになる場合、修正が必要です
            SensorManager.remapCoordinateSystem(inR,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    outR);
            SensorManager.getOrientation(outR, orientationValues);

            Log.v("Orientation", String.valueOf(radianToDegree(orientationValues[0])) + ", " +
                    //Z軸方向,azimuth
                    String.valueOf(radianToDegree(orientationValues[1])) + ", " +
                    //X軸方向,pitch
                    String.valueOf(radianToDegree(orientationValues[2])));       //Y軸方向,roll
        }
    }

    int radianToDegree(float rad) {
        return (int) Math.floor(Math.toDegrees(rad));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public class DataTransfer {

        @JavascriptInterface
        public float[] getAccSensorData() {
            return accelerometerValues;
        }


        @JavascriptInterface
        public float[] getMagSensorData() {
            return magneticValues;
        }
    }






    /*

    @Override
    public void onResume() {
        super.onResume();

        // センサの取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサマネージャへリスナーを登録(implements SensorEventListenerにより、thisで登録する)
        for (Sensor sensor : sensors) {

            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mSensorManager.registerListener((SensorEventListener) this,
                        sensor,
                        SensorManager.SENSOR_DELAY_GAME);
                mIsMagSensor = true;
            }

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mSensorManager.registerListener((SensorEventListener) this,
                        sensor,
                        SensorManager.SENSOR_DELAY_GAME);
                mIsAccSensor = false;
            }
        }
    }

    @Override
    public void onPause() {
        // TODO 自動生成されたメソッド・スタブ
        super.onPause();

        //センサーマネージャのリスナ登録破棄
        if (mIsMagSensor || mIsAccSensor) {
            mSensorManager.unregisterListener(this);
            mIsMagSensor = false;
            mIsAccSensor = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        mGLView =
                new OGLSurfaceView(this.getActivity()); //I believe you may also use getActivity().getApplicationContext();

        return mGLView;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticValues = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values.clone();
                break;
        }

        if (magneticValues != null && accelerometerValues != null) {

            SensorManager.getRotationMatrix(inR, I, accelerometerValues, magneticValues);

            //Activityの表示が縦固定の場合。横向きになる場合、修正が必要です
            SensorManager.remapCoordinateSystem(inR,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    outR);
            SensorManager.getOrientation(outR, orientationValues);

            Log.v("Orientation", String.valueOf(radianToDegree(orientationValues[0])) + ", " +
                    //Z軸方向,azimuth
                    String.valueOf(radianToDegree(orientationValues[1])) + ", " +
                    //X軸方向,pitch
                    String.valueOf(radianToDegree(orientationValues[2])));       //Y軸方向,roll
        }
    }


    int radianToDegree(float rad) {
        return (int) Math.floor(Math.toDegrees(rad));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    */
}