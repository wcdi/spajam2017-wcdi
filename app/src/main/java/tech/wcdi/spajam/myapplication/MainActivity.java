package tech.wcdi.spajam.myapplication;

import android.app.FragmentManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.auth.IdpResponse;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final FragmentManager manager = getFragmentManager();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        manager.beginTransaction()
            .add(
                R.id.content_frame,
                SetupFragment.newInstance(
                    new SetupFragment.OnSetup() {
                        @Override
                        public void apply(IdpResponse response) {
                            manager.beginTransaction()
                                .replace(
                                    R.id.content_frame,
                                    ResourceDownloadingFragment.newInstance(
                                        new ResourceDownloadingFragment.OnSuccess() {
                                            @Override
                                            public void apply(File file) {
                                                manager.beginTransaction()
                                                    .replace(
                                                        R.id.content_frame,
                                                        MainFragment.newInstance()
                                                    )
                                                    .commit();
                                            }
                                        },
                                        new ResourceDownloadingFragment.OnFailure() {
                                            @Override
                                            public void apply() {

                                            }
                                        }
                                    )
                                )
                                .commit();
                        }
                    },
                    new SetupFragment.OnSetupFailed() {
                        @Override
                        public void apply() {
                            MainActivity.this.finish();
                        }
                    }
                ))
            .commit();

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(
            new ArrayAdapter<String>(this, R.layout.drawer_list_item)
        );

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}
