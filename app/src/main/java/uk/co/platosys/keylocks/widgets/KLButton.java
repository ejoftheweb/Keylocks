package uk.co.platosys.keylocks.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;

import uk.co.platosys.keylocks.R;


public class KLButton extends androidx.appcompat.widget.AppCompatButton {
    public KLButton(Context context){
        super(context);
    }
    public KLButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        if(enabled){
            setBackgroundColor(getResources().getColor(R.color.klbutton_enabled));
            setTypeface(getTypeface(), Typeface.NORMAL);
        }else{
            setBackgroundColor(getResources().getColor(R.color.klbutton_disabled));
            setTypeface(getTypeface(), Typeface.ITALIC);
        }
    }

    public void setPreferred(boolean preferred){
        super.setEnabled(preferred);
        if(preferred){
            setBackgroundColor(getResources().getColor(R.color.klbutton_preferred));
            setTypeface(getTypeface(), Typeface.BOLD);
        }else{
            setBackgroundColor(getResources().getColor(R.color.klbutton_enabled));
            setTypeface(getTypeface(), Typeface.NORMAL);
        }
    }

}
