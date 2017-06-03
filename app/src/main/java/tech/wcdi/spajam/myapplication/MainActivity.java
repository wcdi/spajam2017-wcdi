package tech.wcdi.spajam.myapplication;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentManager manager = getFragmentManager();

        manager.beginTransaction()
            .add(R.id.content_frame, MainFragment.newInstance())
            .commit();
    }
}
