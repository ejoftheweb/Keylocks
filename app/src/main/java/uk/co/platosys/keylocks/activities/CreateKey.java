package uk.co.platosys.keylocks.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.PassPhraser;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.utils.Kidney;
import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.widgets.FileChooser;

public class CreateKey extends BaseActivity{

    RadioGroup keyChoice;
    RadioButton createKeyButton;
    RadioButton importKeyButton;
    Toolbar toolbar;
    FloatingActionButton fab;
    Key key;
    GoogleSignInClient googleSignInClient;
    SignInButton signInButton;
    TextView emailTextView;
    private static int RC_SIGN_IN = 9000;
    String email=null;
private static final String TAG = "CK";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_key);
        initialiseViews();
        setSupportActionBar(toolbar);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                                    .requestEmail()
                                                                    .build();
        googleSignInClient= GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        keyChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.create_key_button:
                        showAlert(R.string.key_choice_create_alert_title, R.string.key_choice_create_alert_body, true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        dialog.dismiss();
                                        createKeyPair();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();//TODO
                                        break;
                                    default:
                                        dialog.dismiss();
                                }
                            }
                        });
                        break;
                    case R.id.import_key_button:
                        showAlert(R.string.key_choice_import_alert_title, R.string.key_choice_import_alert_body, true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        dialog.dismiss();
                                        selectKeyFile();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();//TODO
                                        break;
                                    default:
                                        dialog.dismiss();
                                }
                            }
                        });
                        break;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void signIn(){
        Log.e(TAG, "attempting to sign in");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
   }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "rq="+requestCode+" ,rc="+resultCode);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            showAlert("failya", "sign in failed:"+e.getStatusCode(),null);
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode(), e);
            updateUI(null);
        }
    }
    private void selectKeyFile(){
        showAlert("media", Environment.getExternalStorageState(), null);
        switch(Environment.getExternalStorageState()){
            case Environment.MEDIA_MOUNTED:
                chooseFile();
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                chooseFile();
                break;
            default:
                showAlert(R.string.no_external_media_alert_title, R.string.no_external_media_alert_body, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });

        }

    }
    private void chooseFile(){
        FileChooser fileChooser= new FileChooser(CreateKey.this);
        fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                try{
                    key = new Key(file);
                    long keyID=key.getKeyID();
                    Lock lock = null;//TODO
                    String alertBody = getString(R.string.key_import_success_alert_body)+ Kidney.toString(keyID);
                    showAlert(R.string.key_import_success_alert_title, alertBody, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setPassphrase();
                            dialog.dismiss();
                        }
                    });//success
                }catch(MinigmaException moe){
                    showAlert(R.string.key_import_failure_alert_title, R.string.key_import_failure_alert_body, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });//failure
                }
            }
        });
        fileChooser.setExtension(Constants.ASCII_ARMORED_EXTENSION);
        fileChooser.showDialog();
    }

    /**
     *  Starts the key generation process, which should take place in the background.
     *  Keys are generated using a pseudonym and a temporary passphrase.  The user will select and learn a random-word
     *  passphrase which will be applied to the generated Key at a later stage using the temporary
     *  passphrase.
     * @return
     */
    private void createKeyPair (){
        String pseudonym="";
        boolean fileexists=true;
        while(fileexists) {
            pseudonym = new String(PassPhraser.getPassPhrase(1));
            //check to see if the pseudonym has already been taken!
            File keysDirectory = new File(getFilesDir(), Constants.KEYS_DIRECTORY_NAME);
            File keyFile = new File(keysDirectory, pseudonym);
            fileexists=keyFile.exists();
        }

        char[] temppassphrase = (Kidney.toString(Kidney.randomLong(), 'x')).toCharArray();
        LocksmithService.startCreateKeyPair(this, pseudonym, temppassphrase );
        Intent intent = new Intent(this, ChoosePassphrase.class);
        intent.putExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY, temppassphrase);
        intent.putExtra(Constants.USERID_INTENT_KEY, pseudonym);
        startActivity(intent);
    }
    private void setPassphrase(){
        //TODO
    }
    private void initialiseViews(){
        this.keyChoice = (RadioGroup) findViewById(R.id.keychoice);
        this.createKeyButton = (RadioButton) findViewById(R.id.create_key_button);
        this.importKeyButton=(RadioButton) findViewById(R.id.import_key_button);
        this.toolbar=(Toolbar) findViewById(R.id.toolbar);
        this.fab=(FloatingActionButton) findViewById(R.id.fab);
        this.signInButton=(SignInButton) findViewById(R.id.signin_button);
        this.emailTextView=(TextView) findViewById(R.id.emailView);
        keyChoice.setEnabled(false);
        keyChoice.setVisibility(View.INVISIBLE);
    }
    private void updateUI(GoogleSignInAccount account){
        if (account==null){
            signInButton.setEnabled(true);
            signInButton.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.INVISIBLE);
        }else{
           setEmail(account.getEmail());

           keyChoice.setEnabled(true);
           keyChoice.setVisibility(View.VISIBLE);
          signInButton.setEnabled(false);
          signInButton.setVisibility(View.INVISIBLE);
        }

    }
    private void setEmail(String email){
        Log.e(TAG, "set email to:"+email);
        this.email=email;
        emailTextView.setText(email);
        emailTextView.setVisibility(View.VISIBLE);
    }
}
