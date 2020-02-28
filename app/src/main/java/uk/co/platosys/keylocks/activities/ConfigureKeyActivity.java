package uk.co.platosys.keylocks.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.File;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.keylocks.widgets.PassphraseBox;
import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.utils.Kidney;
import uk.co.platosys.minigma.utils.MinigmaUtils;


public class ConfigureKeyActivity extends BaseActivity {
    Intent intent;
    //Views
    TextView keyIDView;
    TextView identitiesViewLabel;
    TextView identitiesView;
    TextView rubricView;
    PassphraseBox passphraseBox;

    long keyID;
    Lock lock;
    Fingerprint fingerprint;

    Toolbar toolbar;
    String TAG = "CKA";
    Key key;
    LocksmithService locksmithService;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadKeyLock(getIntent());
        setContentView(R.layout.activity_configure_key);
        initialiseViews();

        keyIDView.setText(Kidney.toString(keyID));
        toolbar.setTitle(R.string.configure_key_title);
        toolbar.setSubtitle(fingerprint.toString());


        String[] projection = new String[] {
            ContactsContract.Profile._ID,
            ContactsContract.Profile.DISPLAY_NAME,
                ContactsContract.Profile.LOOKUP_KEY
        };
        Cursor profileCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, null,null,null);
        profileCursor.moveToFirst();
        String userDisplayName = profileCursor.getString(profileCursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
        identitiesView.setText(userDisplayName);
    }

    private void loadKeyLock(Intent intent){
        this.keyID = intent.getLongExtra(Constants.KEYID_INTENT_KEY,0);
        char[] temppassphrase=intent.getCharArrayExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY);
        intent.removeExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY);
        char[] passphrase=intent.getCharArrayExtra(Constants.PASSPHRASE_INTENT_KEY);
        intent.removeExtra(Constants.PASSPHRASE_INTENT_KEY);
        try {
            File keyFolder = new File(getFilesDir(), Constants.KEYS_DIRECTORY_NAME);
            key = new Key(new File(keyFolder, MinigmaUtils.encode(keyID)));
            key.changePassphrase(temppassphrase,passphrase);
        }catch(MinigmaException mx){
            Log.e(TAG, "issue initialising key", mx);
        }
        temppassphrase=null;
        passphrase=null;
    }
    private void initialiseViews(){
        this.toolbar=(Toolbar) findViewById(R.id.toolbar);
        this.identitiesViewLabel=(TextView) findViewById(R.id.identities_view_label);
        this.keyIDView=(TextView) findViewById(R.id.id_view);
        this.identitiesView=(TextView) findViewById(R.id.identities_view);
        this.rubricView=(TextView) findViewById(R.id.rubric_box);
        this.passphraseBox =(PassphraseBox) findViewById(R.id.passphrase_box);
    }
    protected void onLockStoreBound(){
        //TODO
    }
    protected void onLockSmithBound(){}
}
