package uk.co.platosys.keylocks.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LockstoreService;


/**Directory Activity is the default Activity for the application. It is an address book.
 *
 *
 */
public class DirectoryActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
     private Toolbar toolbar;
     private FloatingActionButton fab;
     private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private boolean lockstoreBinding;
    private LockstoreService lockstoreService;
    private ServiceConnection lockStoreServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.e("BA", className + " is bound to the lockstore service");
            lockstoreBinding = true;
            lockstoreService=((LockstoreService.LockstoreBinder) iBinder).getService();
            onLockStoreBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            //Log.d("BA", className + " is unbound");
            lockstoreBinding = false;
            lockstoreService=null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        initialiseViews();
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.locks_vouch, R.id.locks_closest, R.id.locks_colleagues,
                R.id.locks_family, R.id.locks_friends, R.id.nav_send)
                .setDrawerLayout(drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.directory, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void initialiseViews(){
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }
    private void onLockStoreBound(){

    }
}
