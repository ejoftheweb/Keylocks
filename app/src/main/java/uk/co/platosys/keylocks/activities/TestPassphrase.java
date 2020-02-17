package uk.co.platosys.keylocks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;


import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.widgets.PassphraseBox;

/** This activity is used to test a passphrase learned in the previous activity.
 *
 *
 */

public class TestPassphrase extends AppCompatActivity {

    TextView nameView;
    TextView emailView;
   PassphraseBox passphraseBox;
    Button passPhraseButton;
    char[] passPhrase;
    //String name;
    Intent intent;
    private String TAG = "TP";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test_passphrase);
        initialiseViews();
        this.intent = getIntent();
        this.passPhrase=intent.getCharArrayExtra(Constants.PASSPHRASE_INTENT_KEY);
        //this.name= intent.getStringExtra(Constants.NAME_INTENT_KEY);
        //nameView.setText(name);

        passPhraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveOn();
            }
        });
    }

    public void moveOn(){}/*
       String stringPassphrase = passphraseBox.getText().toString();
       Log.e(TAG, stringPassphrase);
        stringPassphrase = stringPassphrase.trim();
        char[] enteredPassphrase = stringPassphrase.toCharArray();

        if(Arrays.equals(enteredPassphrase,passPhrase)){
            Intent goOnIntent = new Intent (this, ConfigureKeyActivity.class);
            goOnIntent.putExtra(Constants.PASSPHRASE_INTENT_KEY, passPhrase);
            goOnIntent.putExtra(Constants.USERID_INTENT_KEY, intent.getStringExtra(Constants.USERID_INTENT_KEY));
            goOnIntent.putExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY, intent.getCharArrayExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY));
            startActivity(goOnIntent);
        }else{
            Intent goBackIntent = new Intent(this, LearnPassphrase.class);
            goBackIntent.putExtra(Constants.PASSPHRASE_INTENT_KEY, passPhrase);
            goBackIntent.putExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY, intent.getCharArrayExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY));
             goBackIntent.putExtra(Constants.USERID_INTENT_KEY, intent.getStringExtra(Constants.USERID_INTENT_KEY));
            startActivity(goBackIntent);
        }
    }*/



    private void initialiseViews(){
        this.nameView=(TextView) findViewById(R.id.nameView);
        this.emailView=(TextView) findViewById(R.id.emailView);
        this.passphraseBox = (PassphraseBox) findViewById(R.id.passPhraseBox);
        this.passPhraseButton=(Button) findViewById(R.id.passPhraseButton);
    }
}
