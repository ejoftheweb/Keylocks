package uk.co.platosys.keylocks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.ArrayList;


import uk.co.platosys.keylocks.R;
import uk.co.platosys.minigma.PassPhraser;
import uk.co.platosys.keylocks.Constants;

/**
 * This Activity is part of the startup process and helps the user
 * choose a secure random-word passphrase. The next Activity is LearnPassphrase.
 *
 */
public class ChoosePassphrase extends BaseActivity {

TextView learnPassphraseLabelView;
TextView passphrase1View;
TextView passphrase2View;
TextView passphrase3View;
TextView passPhrase4View;
TextView passPhrase5View;
TextView passPhrase6View;
SeekBar seekBar;
ArrayList<TextView> wordlist = new ArrayList<>();

private Intent nextIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_passphrase);
        initialiseViews();
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
                wordlist.add(passphrase1View);
        wordlist.add(passphrase2View);
        wordlist.add(passphrase3View);
        wordlist.add(passPhrase4View);
        wordlist.add(passPhrase5View);
        wordlist.add(passPhrase6View);
        for(TextView textView:wordlist){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView1 = (TextView)view;
                    char[] passphrase = textView1.getText().toString().toCharArray();
                    confirmPassPhrase(passphrase);
                }
            });
        }


        Intent intent = getIntent();
        nextIntent= new Intent(this, LearnPassphrase.class);
       nextIntent.putExtra(Constants.NAME_INTENT_KEY,intent.getStringExtra(Constants.NAME_INTENT_KEY));
       //TODO nextIntent.putExtra(FPConstants.EMAIL_INTENT_KEY,intent.getStringExtra(FPConstants.EMAIL_INTENT_KEY));
        updatePassPhrases(seekBar.getProgress());
    }

    private void updatePassPhrases(int length){
       for (TextView textView:wordlist){
           textView.setText(new String(PassPhraser.getPassPhrase(length)));
       }
    }
    private void confirmPassPhrase(char[] passPhrase){
       nextIntent.putExtra(Constants.PASSPHRASE_INTENT_KEY,passPhrase);
       startActivity(nextIntent);
    }
    private void initialiseViews(){
        this.learnPassphraseLabelView=(TextView) findViewById(R.id.learnPassphraseLabel);
        this.passphrase1View= (TextView) findViewById(R.id.passPhrase1);
        this.passphrase2View= (TextView) findViewById(R.id.passPhrase2);
        this.passphrase3View= (TextView) findViewById(R.id.passPhrase3);
        this.passPhrase4View= (TextView) findViewById(R.id.passPhrase4);
        this.passPhrase5View= (TextView) findViewById(R.id.passPhrase5);
        this.passPhrase6View= (TextView) findViewById(R.id.passPhrase6);
        this.seekBar= (SeekBar) findViewById(R.id.seekBar);
    }
}
