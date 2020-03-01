package uk.co.platosys.keylocks.models;

import uk.co.platosys.minigma.PassPhraser;

public class IdentityModel implements Identity {
    String textValue;
    String type;
    String typeDesciption;

    public IdentityModel (String textValue, String type, String typeDescription){

    }
    /**
     * this Identity
     */
    @Override
    public String getTextValue() {
        return null;
    }

    /**
     * the type of this identity - typically the reverse domain name of the organisation responsible
     * for managing it.
     *
     * @return
     */
    @Override
    public String getType() {
        return null;
    }

    /**
     * A more user-friendly way of determining the type. Eg for a Twitter type, it might be: "Twitter handle".
     *
     * @return
     */
    @Override
    public String getTypeDescription() {
        return null;
    }
    public static Identity getPseudonymousIdentity(){
        return new IdentityModel(new String(PassPhraser.getPassPhrase(2)),Identity.TYPE_PSEUDONYM,Identity.TYPE_DESCRIPTION_PSEUDONYM);

    }
}
