package uk.co.platosys.keylocks.models;


import uk.co.platosys.keylocks.widgets.Checklist;

/**
 *  An identity is a way of identifying someone, like a name, an email address, a phone number or a
 *  Twitter handle.
 */
public interface Identity extends Checklist.Checklistable {
    /**
     * this Identity
     */
    public String getTextValue();

    /**
     * the type of this identity - typically the reverse domain name of the organisation responsible
     * for managing it.
     *
     * @return
     */
    public String getType(); //eg. com.twitter

    public String getName();

    /**
     * A more user-friendly way of determining the type. Eg for a Twitter type, it might be: "Twitter handle".
     *
     * @return
     */
    public String getTypeDescription(); //

    public static final String TYPE_DESCRIPTION_EMAIL = "Email";
    public static final String TYPE_EMAIL = "smtp.email";

    public static final String TYPE_DESCRIPTION_PHONE = "Phone No:";
    public static final String TYPE_PHONE = "device.phone";

    public static final String TYPE_DESCRIPTION_NAME = "Name";
    public static final String TYPE_NAME = "structured.name";

    public static final String TYPE_DESCRIPTION_PSEUDONYM="Pseudonym";
    public static final String TYPE_PSEUDONYM="anon.pseudonym";

}