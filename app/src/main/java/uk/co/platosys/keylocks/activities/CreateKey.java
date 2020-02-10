package uk.co.platosys.keylocks.activities;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;

import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_key);
        initialiseViews();
        setSupportActionBar(toolbar);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void selectKeyFile(){
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
    }
}
