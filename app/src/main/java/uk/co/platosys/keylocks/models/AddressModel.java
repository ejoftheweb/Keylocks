package uk.co.platosys.keylocks.models;


import java.util.ArrayList;
import java.util.List;

public class AddressModel implements Address{
    private List<Identity> identities = new ArrayList<>();

    public AddressModel(List<Identity> identities){
        this.identities=identities;
    }


    public List<Identity> getIdentities(){
        return identities;
    }
}
