package uk.co.platosys.keylocks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.models.IdentityModel;
import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.keylocks.utils.AccountUtils;
import uk.co.platosys.keylocks.widgets.Checklist;
import uk.co.platosys.keylocks.widgets.ChecklistItem;
import uk.co.platosys.keylocks.widgets.KLButton;
import uk.co.platosys.keylocks.widgets.PassphraseBox;
import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.Exceptions;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.exceptions.MinigmaOtherException;
import uk.co.platosys.minigma.utils.Kidney;

import static uk.co.platosys.keylocks.Constants.ACTION_RESUME;


public class ConfigureKeyActivity extends BaseActivity {
    Intent intent;
    //Views
    Checklist checklist;
    TextView identitiesViewLabel;
    PassphraseBox passphraseBox;
    KLButton leftButton;
    KLButton centreButton;
    KLButton rightButton;
    TextView rubricBox;

    Fingerprint fingerprint;
    Lock lock;
    boolean lockStoreBound;

    List<Checklist.Checklistable> identities = new ArrayList<>();

    Toolbar toolbar;
    String TAG = "CKA";
    Key key;
    LocksmithService locksmithService;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "now creating CKA");
        loadKeyLock(getIntent());
        setContentView(R.layout.activity_configure_key);
        initialiseViews();
        selectIdentities();

    }
    private void selectIdentities(){
        AccountUtils.UserProfile userProfile = AccountUtils.getUserProfile(this);
        identities.addAll(userProfile.getIdentities());
        identities.add(IdentityModel.getPseudonymousIdentity());
        for(Checklist.Checklistable identity:identities){
            ChecklistItem checklistItem = new ChecklistItem(this, identity );


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
        setInitialVisibility();
        toolbar.setTitle(R.string.configure_key_title);
    }
    private void setInitialVisibility(){
        passphraseBox.setVisibility(View.INVISIBLE);
        rightButton.setVisibility(View.INVISIBLE);
        leftButton.setVisibility(View.INVISIBLE);
        centreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmIdentities();
            }
        });
    }
    private void confirmIdentities(){
       checklist.setConfirming();
       identities=checklist.getCheckedItems();
       centreButton.setVisibility(View.INVISIBLE);
       passphraseBox.setVisibility(View.VISIBLE);
      passphraseBox.setOnPassphraseEnteredListener(new PassphraseBox.OnPassphraseEnteredListener() {
          @Override
          public void onPassphraseEntered(PassphraseBox passphraseBox) {
              char[] passphrase=passphraseBox.getPassphrase();
              signKeyLock(passphrase);
          }
      });

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
    private void signKeyLock(char[] passphrase){
        try {
            String alertbody = getResources().getString(R.string.identity_added_alert_body)+Kidney.toString(lock.getFingerprint().getKeyID());
            for (Checklist.Checklistable identity : identities) {
                lock.addID(identity.getTextValue(), key, passphrase);
                showAlert(identity.getTextValue(), alertbody, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            registerConfigurationDone();
        }catch(BadPassphraseException bpe){
            showAlert(R.string.bad_passphrase_alert_title, R.string.bad_passphrase_alert_body, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    confirmIdentities();
                }
            });
        }catch (MinigmaOtherException moe){
            String alertString = getResources().getString(R.string.error_unexpected_alert_body)+ moe.getClass().getName()+ " "+ moe.getMessage();
            showAlert(R.string.error_unexpected_alert_title, alertString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    selectIdentities();
                }
            });
        }

    }
    private void registerConfigurationDone(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFERENCES_FILE,  MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(editor.putInt(Constants.CONFIG_STATE, Constants.CONFIG_STATE_CONFIGURED).commit()){
            Intent intent = new Intent(this, DirectoryActivity.class);
            startActivity(intent);
        }
    }
    private void loadNewKeyLock(Intent intent){
       // Log.e(TAG, "loading newkeylock");
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
        Log.e(TAG, "resuming configuration");
        final Intent intent1=intent;
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
            /*else{
                showAlert(R.string.pause_alert_title, R.string.pause_alert_body, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        resumeConfiguration(intent1);
                    }
                });
            }*/

        }catch(Exception mx){
            Log.e(TAG, "issue initialising key", mx);
        }
    }
    private void initialiseViews(){
        this.toolbar=(Toolbar) findViewById(R.id.toolbar);
        this.identitiesViewLabel=(TextView) findViewById(R.id.identities_view_label);
       this.checklist=(Checklist) findViewById(R.id.checklist);
        this.passphraseBox =(PassphraseBox) findViewById(R.id.passphrase_box);
        this.leftButton=(KLButton) findViewById(R.id.left_button);
        this.centreButton=(KLButton) findViewById(R.id.centre_button);
        this.rightButton=(KLButton)findViewById(R.id.right_button);
        this.rubricBox=(TextView) findViewById(R.id.rubric_box);
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
