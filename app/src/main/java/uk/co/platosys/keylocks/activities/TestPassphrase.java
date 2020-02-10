package uk.co.platosys.keylocks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;


import uk.co.platosys.effwords.EffwordLists;

import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.Constants;

/** This activity is used to test a passphrase learned in the previous activity.
 *
 * TODO move most of this logic into a widget for passphrase entry wherever.
 */

public class TestPassphrase extends AppCompatActivity {

    TextView nameView;
    TextView emailView;
    MultiAutoCompleteTextView multiAutoCompleteTextView;
    Button passPhraseButton;
    char[] passPhrase;
    String name;
    //TODO String email;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_passphrase);
        initialiseViews();
        Intent intent = getIntent();
        this.passPhrase=intent.getCharArrayExtra(Constants.PASSPHRASE_INTENT_KEY);
        this.name= intent.getStringExtra(Constants.NAME_INTENT_KEY);
       //TODO this.email=intent.getStringExtra(FPConstants.EMAIL_INTENT_KEY);
        nameView.setText(name);
        //TODO emailView.setText(email);
        multiAutoCompleteTextView.setTokenizer(new SpaceTokenizer());
        multiAutoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, EffwordLists.getLongWordList()
                ));
        passPhraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveOn();
            }
        });
    }

    public void moveOn(){
        String stringPassphrase =multiAutoCompleteTextView.getText().toString();
        stringPassphrase = stringPassphrase.trim();
        char[] enteredPassphrase = stringPassphrase.toCharArray();

        if(Arrays.equals(enteredPassphrase,passPhrase)){
            Intent goOnIntent = new Intent (this, CreateKey.class);
            goOnIntent.putExtra(Constants.PASSPHRASE_INTENT_KEY, passPhrase);
            //TODO goOnIntent.putExtra(FPConstants.EMAIL_INTENT_KEY, email);
            goOnIntent.putExtra(Constants.NAME_INTENT_KEY, name);
            startActivity(goOnIntent);
        }else{
            Intent goBackIntent = new Intent(this, LearnPassphrase.class);
            goBackIntent.putExtra(Constants.PASSPHRASE_INTENT_KEY, passPhrase);
            //TODO goBackIntent.putExtra(FPConstants.EMAIL_INTENT_KEY, email);
            goBackIntent.putExtra(Constants.NAME_INTENT_KEY, name);
            startActivity(goBackIntent);
        }
    }


    /**
     *  inner class to define the SpaceTokeniser to use for the multi-complete text box.
     */
    private class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && text.charAt(i - 1) != ' ') {i--;}
            while (i < cursor && text.charAt(i) == ' ') {i++;}
            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }
            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();
            while (i > 0 && text.charAt(i - 1) == ' ') {i--;}
            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                     Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }
    private void initialiseViews(){
        this.nameView=(TextView) findViewById(R.id.nameView);
        this.emailView=(TextView) findViewById(R.id.emailView);
        this.multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.passPhraseBox);
        this.passPhraseButton=(Button) findViewById(R.id.passPhraseButton);
    }
}
