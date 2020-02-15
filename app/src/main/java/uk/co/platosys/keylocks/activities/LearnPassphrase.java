package uk.co.platosys.keylocks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.keylocks.R;
import uk.co.platosys.minigma.PassPhraser;

public class LearnPassphrase extends BaseActivity {

TextView rubricTextView;
TextView flashcardTextView;
Button nextButton;
Button hideButton;
private List<String> wordList;
private char[] passphrase;
private boolean flashing=false;
private List<String> rubrics=new ArrayList<>();
Intent nextIntent;
int rubricCounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn__passphrase);
        initialiseViews();
        hideButton.setOnClickListener(startListener);
        Intent intent = getIntent();
        this.passphrase = intent.getCharArrayExtra(Constants.PASSPHRASE_INTENT_KEY);
        this.wordList=PassPhraser.toWordList(passphrase);
        rubrics.add(getString(R.string.passphrase_learn0));
        rubrics.add(getString(R.string.passphrase_learn1));
        rubrics.add(getString(R.string.passphrase_learn2));
        rubrics.add(getString(R.string.passphrase_learn3));

        nextIntent = new Intent(this, TestPassphrase.class);
        nextIntent.putExtra(Constants.PASSPHRASE_INTENT_KEY, passphrase);
        nextIntent.putExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY, intent.getCharArrayExtra(Constants.TEMP_PASSPHRASE_INTENT_KEY));
        nextIntent.putExtra(Constants.USERID_INTENT_KEY,intent.getStringExtra(Constants.USERID_INTENT_KEY));
        //TODO nextIntent.putExtra(FPConstants.EMAIL_INTENT_KEY,intent.getStringExtra(FPConstants.EMAIL_INTENT_KEY));

        nextButton.setEnabled(false);
        nextButton.setOnClickListener(nextListener);


    }
    private  View.OnClickListener startListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Log.i("LPP", "start button pressed");
            Button button = (Button) view;
            button.setText(R.string.button_hide);
            button.setOnClickListener(hideListener);
            startFlashShow();
        }
    };
    private  View.OnClickListener hideListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            button.setText(R.string.button_start);
            button.setOnClickListener(startListener);
            stopFlashShow();
        }
    };
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(nextIntent);
        }
    };
    private void startFlashShow(){
        new Thread(new FlashShow()).start();
    }
    private void stopFlashShow(){
        flashing=false;
        flashcardTextView.setText("");
    }
    private String nextRubric(){
        String rubric = rubrics.get(rubricCounter);
        //Log.i("LPP", rubric);
        rubricCounter++;
        if(rubricCounter==rubrics.size()){
            nextButton.setEnabled(true);
            rubricCounter=0;
        }
        return rubric;

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
                                flashcardTextView.setText(word);
                            }
                        });

                        Thread.sleep(Constants.FLASHWAIT);
                    }
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          flashcardTextView.setText(new String(passphrase));
                                          rubricTextView.setText(nextRubric());
                                      }
                                  });
                    Thread.sleep(Constants.FLASHPAUSE);

                }
            }catch(Exception x){
                Log.e("LPP", "error during flash show", x);
            }
        }
  }
  private void initialiseViews(){
        this.rubricTextView = (TextView) findViewById(R.id.rubric);
        this.flashcardTextView = (TextView) findViewById(R.id.flashchards);
        this.nextButton=(Button) findViewById(R.id.nextbutton);
        this.hideButton=(Button) findViewById(R.id.hidebutton);
  }
}
