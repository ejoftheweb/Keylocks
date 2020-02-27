package uk.co.platosys.keylocks.activities.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddressBookViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddressBookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}