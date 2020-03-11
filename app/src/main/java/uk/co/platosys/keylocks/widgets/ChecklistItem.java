package uk.co.platosys.keylocks.widgets;

import android.content.Context;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import uk.co.platosys.keylocks.R;

public class ChecklistItem extends AppCompatCheckedTextView {
    String name;
    Checklist.Checklistable model;

    public ChecklistItem(Context context, Checklist.Checklistable model){
        super(context);
        this.model=model;
        this.name=model.getName();
        setText(model.getTextValue());
        setChecked(model.isChecked());
        setColours();
        setTextSize(16);
        setPadding(2,2,2,2);
    }
    public String getName(){
        return name;
    }
    @Override
    public void toggle(){
        super.toggle();
        model.setChecked(isChecked());
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
    public void setConfirming(){
        setEnabled(false);
        setTextColor(getResources().getColor(R.color.checklist_item_confirmed_text));
        setBackgroundColor(getResources().getColor(R.color.checklist_item_confirmed_background));
    }
    public Checklist.Checklistable getModel(){
        return model;
    }
}
