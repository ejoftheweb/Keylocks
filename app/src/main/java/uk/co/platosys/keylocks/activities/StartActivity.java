package uk.co.platosys.keylocks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;

/**
 * Activity that justs directs to another depending on the state of the preferences file.
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
        if (sharedPreferences.contains(Constants.DEFAULT_KEY_PREFERENCE)){
            Intent intent = new Intent(this, DirectoryActivity.class);
            startActivity(intent);
        }else {
            //app isn't set up, go to setup activity.
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);
        }
    }
}
