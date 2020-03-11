package uk.co.platosys.keylocks.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.keylocks.widgets.KLButton;
import uk.co.platosys.keylocks.widgets.PassphraseBox;
import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.PassPhraser;
import uk.co.platosys.minigma.utils.MinigmaUtils;

import static uk.co.platosys.keylocks.Constants.ACTION_NEW_KEY;
import static uk.co.platosys.keylocks.Constants.TEMP_PASSPHRASE_INTENT_KEY;

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
    KLButton centreButton;
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
    Fingerprint createdKeyFingerprint = null;
    private static final String BLANK="";

    char[] temppassphrase;
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
        this.centreButton=(KLButton) findViewById(R.id.centre_button);
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
        temppassphrase= MinigmaUtils.encode(new SecureRandom().nextLong()).toCharArray();
    }
    @Override
    public void onKeyCreated(Fingerprint fingerprint){
        keycreated=true;
        createdKeyFingerprint=fingerprint;
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
        this.centreButton.setVisibility(View.INVISIBLE);
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
        this.createOrImportRadioGroup.setEnabled(false);
        this.createKeylockButton.setEnabled(false);
        this.importKeylockButton.setEnabled(false);

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
    protected void onLockSmithBound(){
        locksmithService.addOnKeyCreatedListener(this);
        this.createOrImportRadioGroup.setEnabled(true);
        this.createKeylockButton.setEnabled(true);
        this.importKeylockButton.setEnabled(true);
    }
    protected void onLockStoreBound(){

    }
    private void importKey() {
        showAlert(R.string.feature_not_implemented_alert_title, R.string.feature_not_implementedd_alerd_body, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
    private void choosePassphrase(){
        startCreateKeyPair(this, temppassphrase);
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
        Log.e(TAG, "confirming passphrase:"+new String(passPhrase));
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
        passphraseBox.setVisibility(View.INVISIBLE);
        stopRotatingRubrics();
        rubricCaptionView.setText(R.string.passphrase_learn0);
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
        this.rightButton.setVisibility(View.INVISIBLE);
        this.leftButton.setVisibility(View.INVISIBLE);
        this.wordList=PassPhraser.toWordList(passphrase);
        this.centreButton.setVisibility(View.VISIBLE);
        this.centreButton.setOnClickListener(startListener);
        this.centreButton.setText(R.string.button_start);
    }
    private  View.OnClickListener startListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //Log.i("LPP", "start button pressed");
            KLButton button = (KLButton) view;
            button.setText(R.string.button_hide);
            button.setOnClickListener(hideListener);
            rubricCaptionView.setText("");
            startFlashShow();
        }
    };
    private  View.OnClickListener hideListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            KLButton button = (KLButton) view;
            button.setText(R.string.button_start);
            button.setOnClickListener(startListener);
            rubricCaptionView.setText(R.string.passphrase_learn0);
            stopFlashShow();
        }
    };
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopFlashShow();

           testPassphrase();
        }
    };
    //stage called by the flash show.
    //but do we need a separate timer? count the flashshow repeats?
    private void firstLearningDone(){
        centreButton.setVisibility(View.INVISIBLE);

        leftButton.setVisibility(View.VISIBLE);
        leftButton.setOnClickListener(hideListener);
        leftButton.setEnabled(true);
        leftButton.setText(R.string.button_hide);

        rightButton.setVisibility(View.VISIBLE);
        rightButton.setEnabled(true);
        rightButton.setOnClickListener(nextListener);
        rightButton.setText(R.string.button_got_it);
    }
    private void startFlashShow(){
        new Thread(new FlashShow()).start();
    }
    private void stopFlashShow(){
        flashing=false;
        flashcards.setText("");
        fullPassphrase.setText("");
    }
    private void flashWord(String word){
        if(flashing){
            fullPassphrase.setVisibility(View.INVISIBLE);
            flashcards.setVisibility(View.VISIBLE);
            flashcards.setText(word);
        }else{
            flashcards.setText(BLANK);
        }
    }
    private void flashPhrase(char [] passphrase){
        if(flashing){
            flashcards.setVisibility(View.INVISIBLE);
            fullPassphrase.setVisibility(View.VISIBLE);
            fullPassphrase.setText(new String(passphrase));
        }else{
            fullPassphrase.setText(BLANK);
        }
    }
    private class FlashShow implements Runnable {
        public void run(){
            flashing=true;
            int flashcount = 0;
            try {
                while (flashing) {
                    flashcount++;
                    for (final String word : wordList) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                flashWord(word);
                            }
                        });
                        Thread.sleep(Constants.FLASHWAIT);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            flashPhrase(passphrase);
                        }
                    });
                    Thread.sleep(Constants.FLASHPAUSE);
                    if(flashcount>Constants.FLASH_MINIMUM_REPETITIONS){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                               firstLearningDone();
                            }
                        });
                    }
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
        stopFlashShow();
        hideButtons();
        this.rubricTitleView.setText(rubricTitles.get(trycounter));
        stopRotatingRubrics();

        this.rubricCaptionView.setVisibility(View.INVISIBLE);
        this.flashcards.setVisibility(View.INVISIBLE);
        this.fullPassphrase.setVisibility(View.INVISIBLE);
        this.passphraseBox.setRubric(R.string.rubric_try_passphrase);
        this.passphraseBox.setVisibility(View.VISIBLE);
        this.passphraseBox.clear();
        this.passphraseBox.setClearButtonText(R.string.button_relearn_label);
        this.passphraseBox.setOnPassphraseEnteredListener(new PassphraseBox.OnPassphraseEnteredListener() {
                                                              @Override
                                                              public void onPassphraseEntered(PassphraseBox passphraseBox) {
                                                                  if(Arrays.equals(passphrase, passphraseBox.getPassphrase())){
                                                                      passedTest();
                                                                  }else{
                                                                      failedTest();
                                                                  }
                                                              }
                                                          }

        );
        this.passphraseBox.setOnPassphraseClearedListener(new PassphraseBox.OnPassphraseClearedListener() {
                                                              @Override
                                                              public void onPassphraseCleared(PassphraseBox passphraseBox) {
                                                                  //Log.e(TAG, "passphrase cleared");
                                                                  learnPassphrase();
                                                              }
                                                          }
        );



    }
    public void passedTest(){
         if(trycounter<Constants.PASSPHRASE_TRIES) {
            trycounter++;
            testPassphrase();
        }else if (keycreated) {

            final Intent intent = new Intent(this, ConfigureKeyActivity.class);
            intent.setAction(ACTION_NEW_KEY);
            intent.putExtra(Constants.PASSPHRASE_INTENT_KEY, passphrase);
            intent.putExtra(TEMP_PASSPHRASE_INTENT_KEY, temppassphrase);
            intent.putExtra(Constants.KEYID_INTENT_KEY, createdKeyFingerprint.getFingerprintbytes());
           startActivity(intent);
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
       // Log.e(TAG, "failed test" + new String(passphraseBox.getPassphrase())+" <> "+ new String(passphrase));
        trycounter=0;
        learnPassphrase();
    }
    private void startCreateKeyPair(Context context, char[] temppassphrase) {

       // Log.e(TAG, "using Minigmand version "+ Minigma.VERSION);
        Intent intent = new Intent(context, LocksmithService.class);
        intent.setAction(Constants.CREATE_KEYPAIR);

        intent.putExtra(TEMP_PASSPHRASE_INTENT_KEY, temppassphrase);
        try {
            context.startService(intent);
        }catch (Exception x){
            Log.e(TAG, "context exception ", x);
        }
    }
    private void hideButtons(){
        rightButton.setVisibility(View.INVISIBLE);
        leftButton.setVisibility(View.INVISIBLE);
        centreButton.setVisibility(View.INVISIBLE);
    }

}


