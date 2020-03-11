package uk.co.platosys.keylocks.models;

import uk.co.platosys.minigma.PassPhraser;

public class IdentityModel implements Identity {
    String textValue;
    String type;
    String typeDesciption;
    boolean checked=false;

    public IdentityModel (String textValue, String type, String typeDescription){
        this.textValue=textValue;
        this.type=type;
        this.typeDesciption=typeDescription;
    }
    /**
     * this Identity
     */
    @Override
    public String getTextValue() {
        return textValue;
    }

    /**
     * the type of this identity - typically the reverse domain name of the organisation responsible
     * for managing it.
     *
     * @return
     */
    @Override
    public String getType() {
        return type;
    }
    @Override
    public String getName() {return type;}
    /**
     * A more user-friendly way of determining the type. Eg for a Twitter type, it might be: "Twitter handle".
     *
     * @return
     */
    @Override
    public String getTypeDescription() {
        return typeDesciption;
    }
    public static Identity getPseudonymousIdentity(){
        return new IdentityModel(new String(PassPhraser.getPassPhrase(2)),Identity.TYPE_PSEUDONYM,Identity.TYPE_DESCRIPTION_PSEUDONYM);

    }
    public void setChecked(boolean checked){
        this.checked=checked;
    }
    public boolean isChecked(){
        return checked;
    }
}
