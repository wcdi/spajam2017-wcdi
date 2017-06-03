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
import com.firebase.ui.auth.ResultCodes;

import java.io.Serializable;
import java.util.Arrays;

public class SetupFragment extends Fragment {
    public static interface OnSetup extends Serializable {
        void apply(IdpResponse response);
    }

    public static interface OnSetupFailed extends Serializable {
        void apply();
    }

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
        arguments.putSerializable(OnSetup.class.getName(), onSetup);
        arguments.putSerializable(OnSetupFailed.class.getName(), onSetupFailed);

        SetupFragment fragment = new SetupFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onSetup = (OnSetup) getArguments().getSerializable(OnSetup.class.getName());
        onSetupFailed = (OnSetupFailed) getArguments().getSerializable(OnSetupFailed.class.getName());
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

        if (requestCode == 1000) {
            if (resultCode == ResultCodes.OK)
                onSetup.apply(IdpResponse.fromResultIntent(data));
            else if (resultCode == ResultCodes.CANCELED)
                onSetupFailed.apply();
            else
                onSetupFailed.apply();

        }
    }
}
