package tech.wcdi.spajam.myapplication;

import android.app.Fragment;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.wcdi.spajam.myapplication.Graphics.OGLSurfaceView;

public class MainFragment extends Fragment
{
    private GLSurfaceView mGLView;

    public MainFragment()
    {
        super();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mGLView = new OGLSurfaceView(this.getActivity()); //I believe you may also use getActivity().getApplicationContext();
        return mGLView;
    }
}