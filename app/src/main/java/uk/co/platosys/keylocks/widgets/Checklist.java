package uk.co.platosys.keylocks.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
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

   
}
