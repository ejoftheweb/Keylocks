package uk.co.platosys.keylocks.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.File;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.keylocks.widgets.Checklist;
import uk.co.platosys.keylocks.widgets.ChecklistItem;
import uk.co.platosys.keylocks.widgets.PassphraseBox;
import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.exceptions.Exceptions;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.utils.Kidney;
import uk.co.platosys.minigma.utils.MinigmaUtils;

import static uk.co.platosys.keylocks.Constants.ACTION_RESUME;


public class ConfigureKeyActivity extends BaseActivity {
    Intent intent;
    //Views
    Checklist checklist;
    TextView identitiesViewLabel;
    PassphraseBox passphraseBox;


    Fingerprint fingerprint;
    Lock lock;
    boolean lockStoreBound;


    Toolbar toolbar;
    String TAG = "CKA";
    Key key;
    LocksmithService locksmithService;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadKeyLock(getIntent());
        setContentView(R.layout.activity_configure_key);
        initialiseViews();
        for (Account account : AccountManager.get(this).getAccounts()) {
           ChecklistItem checklistItem = new ChecklistItem(this, account.type, account.name, false);
           checklistItem.setCheckMarkDrawable(R.drawable.ic_menu_send);
           checklistItem.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    ChecklistItem checklistItem = (ChecklistItem) view;
                                                    checklistItem.toggle();
                                                }
                                            }
           );
            checklist.addItem(checklistItem);
        }

        toolbar.setTitle(R.string.configure_key_title);
        toolbar.setSubtitle(Kidney.toString(fingerprint.getKeyID()));

/*do some more to get the phone number here
        String[] projection = new String[] {
            ContactsContract.Profile._ID,
            ContactsContract.Profile.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.DATA1,
                ContactsContract.Profile.LOOKUP_KEY
        };
        Cursor profileCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, null,null,null);
        profileCursor.moveToFirst();
        String userDisplayName = profileCursor.getString(profileCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DATA1));
        identitiesView.setText(userDisplayName);
    }*/
    }
    private void loadKeyLock(Intent intent) {
        switch (intent.getAction()) {
            case Constants.ACTION_NEW_KEY:
                loadNewKeyLock(intent);
            case ACTION_RESUME:
                resumeConfiguration(intent);
            default:
                resumeConfiguration(intent);
        }
    }
    private void loadNewKeyLock(Intent intent){
        this.fingerprint = new Fingerprint(intent.getByteArrayExtra(Constants.KEYID_INTENT_KEY));
        char[] temppassphrase=intent.getCharArrayExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY);
        intent.removeExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY);
        char[] passphrase=intent.getCharArrayExtra(Constants.PASSPHRASE_INTENT_KEY);
        intent.removeExtra(Constants.PASSPHRASE_INTENT_KEY);
        try {
            File keyFolder = new File(getFilesDir(), Constants.KEYS_DIRECTORY_NAME);
            key = new Key(new File(keyFolder, fingerprint.toBase64String()));
            key.changePassphrase(temppassphrase,passphrase);
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
            sharedPreferences.edit().putInt(Constants.CONFIG_STATE, Constants.CONFIG_STATE_KEYLOCK).commit();
            sharedPreferences.edit().putString(Constants.DEFAULT_KEY_PREFERENCE, fingerprint.toBase64String()).commit();
        }catch(MinigmaException mx){
            Log.e(TAG, "issue initialising key", mx);
        }
        temppassphrase=null;
        passphrase=null;
        if ((lock==null)&&(lockStoreBound)){
            onLockStoreBound();
        }
    }
    private void resumeConfiguration(Intent intent){
        try {
            File keyFolder = new File(getFilesDir(), Constants.KEYS_DIRECTORY_NAME);
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
            try {
                key = new Key(new File(keyFolder, sharedPreferences.getString(Constants.DEFAULT_KEY_PREFERENCE, null)));
            }catch(Exception x){
                Exceptions.dump(TAG, "on resume configuration", x);
            }
           this.fingerprint=new Fingerprint(sharedPreferences.getString(Constants.DEFAULT_KEY_PREFERENCE, null));
            if ((lock==null)&&(lockStoreBound)){
                onLockStoreBound();
            }

        }catch(Exception mx){
            Log.e(TAG, "issue initialising key", mx);
        }
    }
    private void initialiseViews(){
        this.toolbar=(Toolbar) findViewById(R.id.toolbar);
        this.identitiesViewLabel=(TextView) findViewById(R.id.identities_view_label);
       this.checklist=(Checklist) findViewById(R.id.checklist);
        this.passphraseBox =(PassphraseBox) findViewById(R.id.passphrase_box);
    }
    protected void onLockStoreBound(){
        lockStoreBound=true;
        lockstoreService.initialise(context);
       if (fingerprint!=null){
        this.lock = lockstoreService.getLock(fingerprint);
        this.toolbar.setSubtitle(Kidney.toString(lock.getLockID()));
       }
    }
    protected void onLockSmithBound(){}
}
