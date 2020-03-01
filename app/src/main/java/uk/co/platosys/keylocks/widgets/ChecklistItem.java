package uk.co.platosys.keylocks.widgets;

import android.content.Context;
import android.widget.CheckedTextView;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import uk.co.platosys.keylocks.R;

public class ChecklistItem extends AppCompatCheckedTextView {
    String name;

    public ChecklistItem(Context context, String name, String text, boolean checked){
        super(context);
        this.name=name;
        setText(text);
        setChecked(checked);
        setColours();
    }
    public String getName(){
        return name;
    }
    @Override
    public void toggle(){
        super.toggle();
        setColours();
    }

    public void setColours(){
        if (isChecked()){
            setTextColor(getResources().getColor(R.color.checklist_item_checked_text));
            setBackgroundColor(getResources().getColor(R.color.checklist_item_checked_background));
        }else{
            setTextColor(getResources().getColor(R.color.checklist_item_unchecked_text));
            setBackgroundColor(getResources().getColor(R.color.checklist_item_unchecked_background));
        }
    }
}
