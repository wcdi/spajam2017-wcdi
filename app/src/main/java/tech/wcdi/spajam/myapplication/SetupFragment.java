package tech.wcdi.spajam.myapplication;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

public class SetupFragment extends Fragment {
    public static interface OnSetup extends Serializable {
        void apply();
    }

    public static final String on_setup_key = "onSetup";

    public OnSetup onSetup;

    public SetupFragment() {
        // Required empty public constructor
    }

    public static SetupFragment newInstance(OnSetup onSetup) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(on_setup_key, onSetup);

        SetupFragment fragment = new SetupFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onSetup = (OnSetup) getArguments().getSerializable(on_setup_key);
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            onSetup.apply();
                        }
                    });
            }
        }
            .start();
    }
}
