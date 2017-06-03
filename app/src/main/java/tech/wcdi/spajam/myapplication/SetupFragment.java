package tech.wcdi.spajam.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.io.Serializable;
import java.util.Arrays;

public class SetupFragment extends Fragment {
    public static interface OnSetup extends Serializable {
        void apply(IdpResponse response);
    }

    public static interface OnSetupFailed extends Serializable {
        void apply();
    }

    public static final String on_setup_key = "on_setup";
    public static final String on_setup_failed_key = "on_setup_failed";

    public OnSetup onSetup;
    public OnSetupFailed onSetupFailed;

    public SetupFragment() {
        // Required empty public constructor
    }

    public static SetupFragment newInstance(
        OnSetup onSetup,
        OnSetupFailed onSetupFailed
    ) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(on_setup_key, onSetup);
        arguments.putSerializable(on_setup_failed_key, onSetupFailed);

        SetupFragment fragment = new SetupFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onSetup = (OnSetup) getArguments().getSerializable(on_setup_key);
        onSetupFailed = (OnSetupFailed) getArguments().getSerializable(on_setup_failed_key);
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

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                ))
                .build(),
            1000
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000)
            onSetup.apply(IdpResponse.fromResultIntent(data));
    }
}
