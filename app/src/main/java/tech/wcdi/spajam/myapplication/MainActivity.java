package tech.wcdi.spajam.myapplication;

import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final FragmentManager manager = getFragmentManager();

        manager.beginTransaction()
            .add(
                R.id.content_frame,
                SetupFragment.newInstance(
                    new SetupFragment.OnSetup() {
                        @Override
                        public void apply(IdpResponse response) {
                            Toast
                                .makeText(
                                    MainActivity.this,
                                    response.getEmail(),
                                    Toast.LENGTH_LONG
                                )
                                .show();

                            manager.beginTransaction()
                                .replace(R.id.content_frame, MainFragment.newInstance())
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
