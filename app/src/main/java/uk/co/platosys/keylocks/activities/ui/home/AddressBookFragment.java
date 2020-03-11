package uk.co.platosys.keylocks.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import uk.co.platosys.keylocks.R;

public class AddressBookFragment extends Fragment {

    private AddressBookViewModel addressBookViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addressBookViewModel  =
                ViewModelProviders.of(this).get(AddressBookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final RecyclerView recyclerView = root.findViewById(R.id.address_recycler);

        addressBookViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}