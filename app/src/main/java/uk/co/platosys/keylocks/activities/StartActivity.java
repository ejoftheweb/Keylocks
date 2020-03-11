package uk.co.platosys.keylocks.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;

import static uk.co.platosys.keylocks.Constants.ACTION_RESUME;

/**
 * Activity that justs directs to another depending on the state of the preferences file.
 */
public class StartActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = "StartActivity";
    private TextView startStatusView;
    private Button startButton;
    Intent intent;
    String[] requiredPermissions=new String[]{
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this.startStatusView=findViewById(R.id.start_status);
        this.startButton=findViewById(R.id.start_button);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, requiredPermissions, Constants.CONTACT_PERMISSIONS_REQUEST);
        }else{
            proceed();
        }
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] requiredPermissions, int[] result){
        switch(requestCode){
            case Constants.CONTACT_PERMISSIONS_REQUEST:
                Log.e(TAG, "permissions received");
                proceed(requiredPermissions,result);
                break;
            default:
                halt();
        }
    }
    public void proceed(String[] requiredPermissions, int[] result) {
        for (int i = 0; i < requiredPermissions.length; i++) {
            if (result[i] == PackageManager.PERMISSION_DENIED) {
                halt();
            }
        }
        proceed();
    }
    private void proceed(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
        if(!(sharedPreferences.contains(Constants.CONFIG_STATE))){
            sharedPreferences.edit().putInt(Constants.CONFIG_STATE, Constants.CONFIG_STATE_NONE).commit();
        }

            //Intent intent;
            switch (sharedPreferences.getInt(Constants.CONFIG_STATE, Constants.DEFAULT_CONFIG_STATE)){
                case Constants.CONFIG_STATE_NONE:
                    startStatusView.setText("Not configured");
                    intent=new Intent(this, SetupActivity.class);
                    break;
                case Constants.CONFIG_STATE_KEYLOCK:
                    startStatusView.setText(sharedPreferences.getString(Constants.DEFAULT_KEY_PREFERENCE, null));
                    intent=new Intent(this, ConfigureKeyActivity.class);
                    intent.setAction(ACTION_RESUME);
                    break;
                case Constants.CONFIG_STATE_CONFIGURED:
                default:
                    intent = new Intent(this, DirectoryActivity.class);
                    break;
            }
        intent.putExtra (sharedPreferences.getString(Constants.DEFAULT_KEY_PREFERENCE, null),"");
         //startActivity(intent);

    }
    public void halt(){
        Intent intent = new Intent(this, ClosedownActivity.class);
        startActivity(intent);

    }
}
