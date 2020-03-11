package uk.co.platosys.keylocks.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Checklist extends LinearLayout {
    public List<ChecklistItem> items=new ArrayList<>();
    public Checklist (Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public void addItem(ChecklistItem item){
        super.addView(item);
        items.add(item);
    }
    public List<Checklistable> getCheckedItems(){
        List<Checklistable> returnedList = new ArrayList<>();
        for(ChecklistItem checklistItem:items){
            if(checklistItem.isChecked()){
                returnedList.add(checklistItem.getModel());
            }
        }
        return returnedList;
    }
    public void setConfirming(){
        for (ChecklistItem checklistItem:items){
            if(checklistItem.isChecked()){
                checklistItem.setConfirming();
            }else {
                checklistItem.setVisibility(INVISIBLE);
            }
        }
    }
    public interface Checklistable {
        public boolean isChecked();
        public void setChecked(boolean checked);
        public String getName();
        public String getTextValue();
    }
   
}
