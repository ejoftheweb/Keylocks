package uk.co.platosys.keylocks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;

import static android.content.Context.MODE_PRIVATE;
import static uk.co.platosys.keylocks.Constants.ACTION_RESUME;

/**
 * Activity that justs directs to another depending on the state of the preferences file.
 */
public class StartActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = "StartActivity";
    String[] requiredPermissions=new String[]{
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        for(Account account: AccountManager.get(this).getAccounts()) {
                Log.e("TATAG",  account.name + " " + account.toString());
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, requiredPermissions, Constants.CONTACT_PERMISSIONS_REQUEST);
        }else{
            proceed();
        }
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

            Intent intent;
            switch (sharedPreferences.getInt(Constants.CONFIG_STATE, Constants.DEFAULT_CONFIG_STATE)){
                case Constants.CONFIG_STATE_NONE:
                    intent=new Intent(this, SetupActivity.class);
                    break;
                case Constants.CONFIG_STATE_KEYLOCK:
                    intent=new Intent(this, ConfigureKeyActivity.class);
                    intent.setAction(ACTION_RESUME);
                    break;
                case Constants.CONFIG_STATE_CONFIGURED:
                default:
                    intent = new Intent(this, DirectoryActivity.class);
                    break;
            }
        intent.putExtra (sharedPreferences.getString(Constants.DEFAULT_KEY_PREFERENCE, null),"");
         startActivity(intent);

    }
    public void halt(){
        Intent intent = new Intent(this, ClosedownActivity.class);
        startActivity(intent);

    }
}
