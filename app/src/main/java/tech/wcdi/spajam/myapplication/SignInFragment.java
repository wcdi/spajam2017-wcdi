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

public class SignInFragment extends Fragment {
    public static interface OnSignIn extends Serializable {
        void apply(IdpResponse response);
    }

    public static interface OnSignInFailed extends Serializable {
        void apply();
    }

    public OnSignIn onSignIn;
    public OnSignInFailed onSignInFailed;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(
        OnSignIn onSetup,
        OnSignInFailed onSetupFailed
    ) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(OnSignIn.class.getName(), onSetup);
        arguments.putSerializable(OnSignInFailed.class.getName(), onSetupFailed);

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onSignIn = (OnSignIn) getArguments().getSerializable(OnSignIn.class.getName());
        onSignInFailed = (OnSignInFailed) getArguments().getSerializable(OnSignInFailed.class.getName());
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
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
                onSignIn.apply(IdpResponse.fromResultIntent(data));
            else if (resultCode == ResultCodes.CANCELED)
                onSignInFailed.apply();
            else
                onSignInFailed.apply();
        }
    }
}
