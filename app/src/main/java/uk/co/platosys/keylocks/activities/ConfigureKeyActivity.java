package uk.co.platosys.keylocks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.File;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.keylocks.widgets.PassphraseBox;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.utils.Kidney;


public class ConfigureKeyActivity extends BaseActivity implements LocksmithService.OnKeyCreatedListener {
    Intent intent;
    //Views
    TextView keyIDView;
    TextView identitiesViewLabel;
    TextView identitiesView;
    TextView rubricView;
    PassphraseBox passphraseBox;
    Toolbar toolbar;
    String pseudonym;
    String TAG = "CKA";
    Key key;
    LocksmithService locksmithService;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.intent=getIntent();
        setContentView(R.layout.content_configure_key);
        initialiseViews();
        this.pseudonym=intent.getStringExtra(Constants.USERID_INTENT_KEY);
        identitiesView.setText(pseudonym);
        while(!bound){
            try {
                wait(100);
            }catch(Exception x){}
        }
        locksmithService.addOnKeyCreatedListener(this);


    }
    public void onKeyCreated(long keyID){
        try {
            this.key = new Key(new File(new File(getFilesDir(), Constants.KEYS_DIRECTORY_NAME), pseudonym));
            keyIDView.setText(Kidney.toString(key.getKeyID()));
        }catch(MinigmaException mx) {
            Log.e(TAG, "problem initialising key", mx);
        }
    }
    private void initialiseViews(){
        this.toolbar=(Toolbar) findViewById(R.id.toolbar);
        this.identitiesViewLabel=(TextView) findViewById(R.id.identities_view_label);
        this.keyIDView=(TextView) findViewById(R.id.id_view);
        this.identitiesView=(TextView) findViewById(R.id.identities_view);
        this.rubricView=(TextView) findViewById(R.id.rubric_box);
        this.passphraseBox =(PassphraseBox) findViewById(R.id.passphrase_box);
    }
}
