package uk.co.platosys.keylocks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.keylocks.widgets.KLButton;
import uk.co.platosys.keylocks.widgets.PassphraseBox;
import uk.co.platosys.minigma.PassPhraser;

public class SetupActivity extends BaseActivity implements LocksmithService.OnKeyCreatedListener {
    TextView rubricTitleView;
    TextView rubricCaptionView;
    ImageView rubricIllustrationView;
    RadioButton createKeylockButton;
    RadioButton importKeylockButton;
    RadioGroup createOrImportRadioGroup;
   TextView lessSecure;
    TextView moreSecure;
    SeekBar seekBar;
    TextView passphrase0;
    TextView passphrase1;
    TextView passphrase2;
    TextView passphrase3;
    TextView passphrase4;
    TextView passphrase5;
    TextView flashcards;
    KLButton leftButton;
    KLButton rightButton;
    TextView fullPassphrase;
    PassphraseBox passphraseBox;
    ArrayList<TextView> passphraseList = new ArrayList<>();
    int trycounter=0;
    private char[] passphrase;
    private boolean flashing=false;
    private List<String> rubrics=new ArrayList<>();
    private List<String> wordList=new ArrayList<>();
    private List<String> rubricTitles=new ArrayList<>();
    int rubricCounter=0;
    private boolean rotating_rubrices=false;
    private boolean keycreated=false;
    long createdKeyID = 0;

    private String TAG="SetupActivity";

    private void initialiseViews() {
        this.rubricTitleView = (TextView) findViewById(R.id.rubric_title_view);
        this.rubricCaptionView = (TextView) findViewById(R.id.rubric_caption_view);
        this.rubricIllustrationView = (ImageView) findViewById(R.id.rubric_illustration_view);
        this.createOrImportRadioGroup = (RadioGroup) findViewById(R.id.create_or_import_radio_group);
        this.createKeylockButton = (RadioButton) findViewById(R.id.new_keylock_radio_button);
        this.importKeylockButton = (RadioButton) findViewById(R.id.import_keylock_radio_button);
        this.lessSecure=(TextView) findViewById(R.id.less_secure);
        this.moreSecure=(TextView) findViewById(R.id.more_secure);
        this.seekBar=(SeekBar) findViewById(R.id.passphrase_length_seek_bar);
        this.passphrase0=(TextView) findViewById(R.id.passphrase_0);
        this.passphrase1=(TextView) findViewById(R.id.passphrase_1);
        this.passphrase2=(TextView) findViewById(R.id.passphrase_2);
        this.passphrase3=(TextView) findViewById(R.id.passphrase_3);
        this.passphrase4=(TextView) findViewById(R.id.passphrase_4);
        this.passphrase5=(TextView) findViewById(R.id.passphrase_5);
        this.flashcards=(TextView) findViewById(R.id.flashchards);
        this.leftButton=(KLButton) findViewById(R.id.left_button);
        this.rightButton=(KLButton) findViewById(R.id.right_button);
        this.passphraseBox=(PassphraseBox) findViewById(R.id.passphrase_box);
        this.fullPassphrase=(TextView) findViewById(R.id.full_passphrase);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        initialiseViews();
        setInitialListeners();
        setOpeningVisibility();
        fillLists();
        doBinding();
        LocksmithService.startCreateKeyPair(this, );
    }
    @Override
    public void onKeyCreated(long keyID){
        keycreated=true;
        Toast.makeText(this, R.string.keylock_created_toast, Toast.LENGTH_SHORT);
    }
    private void doBinding(){
        while (!bound){
            try {
                wait(50);
            }catch(InterruptedException ie){}
        locksmithService.addOnKeyCreatedListener(this);
       }
    }

    //Add listeners:
    private void setInitialListeners() {
        this.rightButton.setText(R.string.setup_button_go);
        this.rightButton.setPreferred(true);
        this.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetup();
            }
        });
        this.leftButton.setText(R.string.setup_button_later);
        this.leftButton.setPreferred(false);
        this.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//

    /**
     * This method sets all Views not required for the first stage of setup to INVISIBLE. They are made
     * VISIBLE as required as SetupActivity makes its way through the sequence of setup methods.
     */
    private void setOpeningVisibility() {
        this.createKeylockButton.setVisibility(View.INVISIBLE);
        this.importKeylockButton.setVisibility(View.INVISIBLE);
        lessSecure.setVisibility(View.INVISIBLE);
        moreSecure.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
        this.lessSecure.setVisibility(View.INVISIBLE);
        this.moreSecure.setVisibility(View.INVISIBLE);
        this.seekBar.setVisibility(View.INVISIBLE);
        this.passphrase0.setVisibility(View.INVISIBLE);
        this.passphrase1.setVisibility(View.INVISIBLE);
        this.passphrase2.setVisibility(View.INVISIBLE);
        this.passphrase3.setVisibility(View.INVISIBLE);
        this.passphrase4.setVisibility(View.INVISIBLE);
        this.passphrase5.setVisibility(View.INVISIBLE);
        this.flashcards.setVisibility(View.INVISIBLE);
        this.fullPassphrase.setVisibility(View.INVISIBLE);
        this.passphraseBox.setVisibility(View.INVISIBLE);

        //later stage widgets too!
    }
    private void fillLists(){
        rubricTitles.add(getString(R.string.rubric_test_passphrase_title_0));
        rubricTitles.add(getString(R.string.rubric_test_passphrase_title_1));
        rubricTitles.add(getString(R.string.rubric_test_passphrase_title_2));
    }

    private void startSetup() {
        this.rightButton.setVisibility(View.INVISIBLE);
        this.leftButton.setVisibility(View.INVISIBLE);

        this.createKeylockButton.setVisibility(View.VISIBLE);
        this.importKeylockButton.setVisibility(View.VISIBLE);
        this.rubricTitleView.setText(R.string.rubric_setup_pgp_title);
        this.rubricCaptionView.setText(R.string.rubric_setup_pgp_body);
        this.createOrImportRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int which) {
                switch (which) {
                    case (R.id.new_keylock_radio_button):
                        choosePassphrase();
                        break;
                    case (R.id.import_keylock_radio_button):
                        importKey();
                        break;
                    default:
                        //do nothing.
                }
            }
        });
    }

    private void importKey() {
        //TODO
    }
    private void choosePassphrase(){
        this.createKeylockButton.setVisibility(View.INVISIBLE);
        this.importKeylockButton.setVisibility(View.INVISIBLE);
        this.lessSecure.setVisibility(View.VISIBLE);
        this.seekBar.setVisibility(View.VISIBLE);
        this.moreSecure.setVisibility(View.VISIBLE);
        this.passphrase0.setVisibility(View.VISIBLE);
        this.passphrase1.setVisibility(View.VISIBLE);
        this.passphrase2.setVisibility(View.VISIBLE);
        this.passphrase3.setVisibility(View.VISIBLE);
        this.passphrase4.setVisibility(View.VISIBLE);
        this.passphrase5.setVisibility(View.VISIBLE);
        this.rubricTitleView.setText(R.string.rubric_choose_passphrase_title);
        rubrics.add(getString(R.string.rubric_choose_passphrase_body_0));
        rubrics.add(getString(R.string.rubric_choose_passphrase_body_1));
        rubrics.add(getString(R.string.rubric_choose_passphrase_body_2));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if(progress>(Constants.MIN_PASSPHRASE_LENGTH-1)) {
                        updatePassPhrases(progress);
                    }else{
                        seekBar.setProgress(Constants.MIN_PASSPHRASE_LENGTH);
                        updatePassPhrases(Constants.MIN_PASSPHRASE_LENGTH);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        passphraseList.add(passphrase0);
        passphraseList.add(passphrase1);
        passphraseList.add(passphrase2);
        passphraseList.add(passphrase3);
        passphraseList.add(passphrase4);
        passphraseList.add(passphrase5);
        for(TextView textView:passphraseList){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView1 = (TextView)view;
                    Log.e(TAG, "passphrase chosen");
                    char[] passphrase = textView1.getText().toString().toCharArray();
                    confirmPassPhrase(passphrase);
                }
            });
        }
        updatePassPhrases(Constants.DEFAULT_PASSPHRASE_LENGTH);
        startRotatingRubrics();

    }
    private void updatePassPhrases(int length){
        Log.e(TAG, "passphrases changed to length:"+length);
        for (TextView textView:passphraseList){
            textView.setText(new String(PassPhraser.getPassPhrase(length)));
        }
    }
    private void confirmPassPhrase(final char[] passPhrase){
        Log.e(TAG, "confirming passphrase:"+passPhrase);
        this.passphrase = passPhrase;
        showAlert(R.string.confirm_passphrase_alert_title, new String(passPhrase), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int button) {
                switch (button){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialogInterface.dismiss();
                        learnPassphrase();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        updatePassPhrases(Constants.DEFAULT_PASSPHRASE_LENGTH);
                    default:
                        dialogInterface.dismiss();
                }
            }
        });
    }
    private void learnPassphrase(){
        this.rubricTitleView.setText(R.string.rubric_learn_passphrase_title);
        this.lessSecure.setVisibility(View.INVISIBLE);
        this.seekBar.setVisibility(View.INVISIBLE);
        this.moreSecure.setVisibility(View.INVISIBLE);
        this.passphrase0.setVisibility(View.INVISIBLE);
        this.passphrase1.setVisibility(View.INVISIBLE);
        this.passphrase2.setVisibility(View.INVISIBLE);
        this.passphrase3.setVisibility(View.INVISIBLE);
        this.passphrase4.setVisibility(View.INVISIBLE);
        this.passphrase5.setVisibility(View.INVISIBLE);
        this.rubricIllustrationView.setVisibility(View.INVISIBLE);
        this.flashcards.setVisibility(View.VISIBLE);
        this.rightButton.setVisibility(View.VISIBLE);
        this.leftButton.setVisibility(View.VISIBLE);
        this.wordList=PassPhraser.toWordList(passphrase);
        rubrics=new ArrayList<>();
        rubrics.add(getString(R.string.passphrase_learn0));
        rubrics.add(getString(R.string.passphrase_learn1));
        rubrics.add(getString(R.string.passphrase_learn2));
        rubrics.add(getString(R.string.passphrase_learn3));
        rightButton.setEnabled(false);

        rightButton.setOnClickListener(nextListener);
        rightButton.setText(R.string.button_next);
        leftButton.setOnClickListener(startListener);
        leftButton.setPreferred(true);
        leftButton.setText(R.string.button_start);
    }
    private  View.OnClickListener startListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Log.i("LPP", "start button pressed");
            KLButton button = (KLButton) view;
            button.setText(R.string.button_hide);
            button.setOnClickListener(hideListener);
            startFlashShow();
        }
    };
    private  View.OnClickListener hideListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            KLButton button = (KLButton) view;
            button.setText(R.string.button_start);
            button.setOnClickListener(startListener);
            stopFlashShow();
        }
    };
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           testPassphrase();
        }
    };
    private void startFlashShow(){
        new Thread(new FlashShow()).start();
    }
    private void stopFlashShow(){
        flashing=false;
        flashcards.setText("");
    }
    private class FlashShow implements Runnable {
        public void run(){
            flashing=true;
            try {
                while (flashing) {
                    for (final String word : wordList) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fullPassphrase.setVisibility(View.INVISIBLE);
                                flashcards.setVisibility(View.VISIBLE);
                                flashcards.setText(word);
                            }
                        });

                        Thread.sleep(Constants.FLASHWAIT);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            flashcards.setVisibility(View.INVISIBLE);
                            fullPassphrase.setVisibility(View.VISIBLE);
                            fullPassphrase.setText(new String(passphrase));
                            rubricCaptionView.setText(nextRubric());
                        }
                    });
                    Thread.sleep(Constants.FLASHPAUSE);
                    rightButton.setPreferred(true);
                    leftButton.setPreferred(false);
                    flashcards.setVisibility(View.VISIBLE);
                    fullPassphrase.setVisibility(View.INVISIBLE);
                }
            }catch(Exception x){
                Log.e("LPP", "error during flash show", x);
            }
        }
    }
    private void startRotatingRubrics(){new Thread(new RubricRotator()).start();}
    private void stopRotatingRubrics(){
        rotating_rubrices=false;

    }
    private class RubricRotator implements Runnable {
        public void run(){
            rotating_rubrices=true;
            try {
                while (rotating_rubrices) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //flashcards.setText(new String(passphrase));
                            rubricCaptionView.setText(nextRubric());
                        }
                    });
                    Thread.sleep(Constants.FLASHPAUSE);

                }
            }catch(Exception x){
                Log.e("LPP", "error during flash show", x);
            }
        }
    }
    private String nextRubric(){
        String rubric = rubrics.get(rubricCounter);
        //Log.i("LPP", rubric);
        rubricCounter++;
        if(rubricCounter==rubrics.size()){
            rightButton.setEnabled(true);
            rubricCounter=0;
        }
        return rubric;
    }

    private void testPassphrase(){

        this.rubricTitleView.setText(rubricTitles.get(trycounter));
        this.rubricCaptionView.setVisibility(View.INVISIBLE);
        this.flashcards.setVisibility(View.INVISIBLE);
        this.fullPassphrase.setVisibility(View.INVISIBLE);
        this.passphraseBox.setVisibility(View.VISIBLE);
        this.passphraseBox.clearComposingText();
        leftButton.setText(R.string.button_relearn_label);
        leftButton.setPreferred(true);
        rightButton.setEnabled(true);
        rightButton.setText(R.string.OK);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Arrays.equals(passphraseBox.getPassphrase(),passphrase)){
                    passedTest();
                }else{
                    failedTest();
                }
            }
        });

    }
    public void passedTest(){

        if(trycounter<Constants.PASSPHRASE_TRIES) {
            trycounter++;
            learnPassphrase();
        }else if (keycreated) {
            rightButton.setText(R.string.button_next);
            rightButton.setPreferred(true);
            final Intent intent = new Intent(this, ConfigureKeyActivity.class);
            intent.putExtra(Constants.PASSPHRASE_INTENT_KEY, passphrase);
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }else {
            showAlert(R.string.keylock_not_ready_alert_title, R.string.keylock_not_ready_alert_body, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    testPassphrase();
                }
            });
        }
    }
    public void failedTest(){
        trycounter=0;
        learnPassphrase();
    }

}


