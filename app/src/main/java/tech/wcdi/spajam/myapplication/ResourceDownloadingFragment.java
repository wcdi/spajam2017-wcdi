package tech.wcdi.spajam.myapplication;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;

import java.io.File;
import java.io.Serializable;

public class ResourceDownloadingFragment extends Fragment {
    public static interface OnSuccess extends Serializable {
        public void apply(File file);
    }

    public static interface OnFailure extends Serializable {
        public void apply();
    }

    public OnSuccess onSuccess;

    public OnFailure onFailure;

    public ResourceDownloadingFragment() {
        // Required empty public constructor
    }

    public static ResourceDownloadingFragment newInstance(
        OnSuccess onSuccess,
        OnFailure onFailure
    ) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(OnSuccess.class.getName(), onSuccess);
        arguments.putSerializable(OnFailure.class.getName(), onFailure);

        ResourceDownloadingFragment fragment = new ResourceDownloadingFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onSuccess = (OnSuccess) getArguments().getSerializable(OnSuccess.class.getName());
        onFailure = (OnFailure) getArguments().getSerializable(OnFailure.class.getName());
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resource_downloading, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final File file = new File(getContext().getFilesDir(), "hip.db");


        if (file.exists());
            onSuccess.apply(file);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(100);

        FirebaseStorage.getInstance()
            .getReferenceFromUrl("gs://firebase-spajam2017.appspot.com/hip.db")
            .getFile(file)
            .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    progressBar.setProgress((int) progress);
                }
            })
            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    onSuccess.apply(file);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    onFailure.apply();
                }
            });
    }

    ;


}
