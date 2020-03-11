package uk.co.platosys.keylocks.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.effwords.EffwordLists;
import uk.co.platosys.keylocks.R;

/** Widget displaying a passphrase box for entering random-word passphrases. Implement and attach an OnPassphraseEnteredListener
 *  to collect the  entered passphrase and move to the next stage.
 *
 */
public class PassphraseBox extends ConstraintLayout {
    private char[] passphrase;
    private CharSequence blank = new String("");
    boolean set = false;
    private String TAG = "PPBox";
    private TextView wordCounterView;
    private TextView rubricView;
    private AppCompatAutoCompleteTextView entryView;
    private KLButton okButton;
    private KLButton clearButton;
    private List<CharSequence> wordlist = new ArrayList<>();
    private static final CharSequence COUNTER_CHARS = " *";
    private static final String SPACE=" ";
    private OnPassphraseEnteredListener onPassphraseEnteredListener;
    private OnPassphraseClearedListener onPassphraseClearedListener;

    public PassphraseBox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.passphrase_box, this);
        initialiseViews();

        //Log.e(TAG, "creating passphrease-box");

        // entryView.setTokenizer(new SpaceTokenizer());

        entryView.setAdapter(new ArrayAdapter<String>(context,
                R.layout.support_simple_spinner_dropdown_item, EffwordLists.getLongWordList()
        ));

        entryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.e(TAG, "item-click-listener-triggered");
                TextView textView = (TextView) view;
                wordlist.add(textView.getText());
                wordCounterView.append(COUNTER_CHARS);//replace
                entryView.setText("");

            }
       });

        clearButton.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               //Log.e(TAG,"clear button clicked");
                                               PassphraseBox.this.onPassphraseCleared();
                                              clear();
                                           }
                                       }
        );
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer buffer = new StringBuffer();
                for(CharSequence word:wordlist){
                    buffer.append(word);
                    buffer.append(SPACE);
                }
                PassphraseBox.this.passphrase=new String(buffer).trim().toCharArray();
                PassphraseBox.this.onPassphraseEntered();
                clear();
            }
        });

    }

    public char[] getPassphrase() {
         return passphrase;
    }

    public void clear(){
        wordlist.clear();
        wordCounterView.setText("");
        entryView.setText("");
    }
    /**Listener called when the "ok" button is pressed*/
    public interface OnPassphraseEnteredListener {
        public void onPassphraseEntered(PassphraseBox passphraseBox);
    }
    /**Listener called when the "clear" button is pressed*/
    public interface OnPassphraseClearedListener {
        public void onPassphraseCleared(PassphraseBox passphraseBox);
    }
    public void setOnPassphraseEnteredListener (OnPassphraseEnteredListener onPassphraseEnteredListener){
        this.onPassphraseEnteredListener=onPassphraseEnteredListener;
    }
    public void setOnPassphraseClearedListener (OnPassphraseClearedListener onPassphraseClearedListener){
        this.onPassphraseClearedListener = onPassphraseClearedListener;
    }
    private void onPassphraseEntered(){
        if(onPassphraseEnteredListener!=null) {
            onPassphraseEnteredListener.onPassphraseEntered(this);
        }
    }
    private void onPassphraseCleared(){
        clear();
        if(onPassphraseClearedListener!=null) {
            onPassphraseClearedListener.onPassphraseCleared(this);
        }
    }
    public void setClearButtonText(int resourceID){
        this.clearButton.setText(resourceID);
    }
    public void setOkButtonText(int resourceID){
        this.okButton.setText(resourceID);
    }
    public void setRubric(int resourceID){
        this.rubricView.setText(resourceID);
    }
    private void initialiseViews() {
        this.wordCounterView = (TextView) findViewById(R.id.passphrase_wordcount);
        this.rubricView = (TextView) findViewById(R.id.passphrase_rubric);
        this.entryView = (AppCompatAutoCompleteTextView) findViewById(R.id.pbox_entry);
        this.okButton = (KLButton) findViewById(R.id.pbox_ok_button);
        this.clearButton = (KLButton) findViewById(R.id.pbox_clear_button);
        entryView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }
}
