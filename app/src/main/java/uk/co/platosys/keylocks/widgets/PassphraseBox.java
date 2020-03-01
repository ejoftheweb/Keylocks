package uk.co.platosys.keylocks.widgets;

import android.content.Context;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import uk.co.platosys.effwords.EffwordLists;
import uk.co.platosys.keylocks.R;

public class PassphraseBox extends AppCompatMultiAutoCompleteTextView {
    private char[] passphrase;
    private CharSequence blank = new String("");
    boolean set = false;
    private String TAG = "PPBox";
    public PassphraseBox(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setTokenizer(new SpaceTokenizer());
        setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setAdapter(new ArrayAdapter<String>(context,
                R.layout.support_simple_spinner_dropdown_item, EffwordLists.getLongWordList()
        ));
    }
    public char[] getPassphrase(){
        if(set) {
            return passphrase;

        }else{
            set();
            return passphrase;
        }
    }

    /** Clears the display but sets the passphrase variable which can
     * still be reached using the getPassphrase() method.
     *
     */
    public void set(){
        String text = getText().toString().trim();
        this.passphrase= text.toCharArray();
        setText(blank);
        set=true;
    }
    public void clear(){
        setText(blank);
        this.passphrase=getText().toString().toCharArray();
        set=false;
    }
    public void setExpectsInput(boolean expectsInput){

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
                    SpannableString spannableString = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, spannableString, 0);
                    return spannableString;
                } else {
                    return text + " ";
                }
            }
        }
    }
}
