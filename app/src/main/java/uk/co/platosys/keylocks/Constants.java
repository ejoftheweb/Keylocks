package uk.co.platosys.keylocks;

/**
 * Created by edward on 13/02/18.
 */

public class Constants {
    public static final String HKP_PROTOCOL = "https:";
    public static final String HKP_HOST ="keyserver.ubuntu.com";
    public static final int HKP_PORT=11371;
    public static final String HKP_FILE="";



    //
    public static final String KEYS_DIRECTORY_NAME="farina";
    public static final String LOCKSTORE_FILE_NAME="lockstore";





    public static final String PREFS_USERNAME_KEY="username";

    public static final int MIN_PASSPHRASE_LENGTH=1;//for production, increase to 4
    public static final int DEFAULT_PASSPHRASE_LENGTH=5;
    public static final int PASSPHRASE_TRIES=2;
    //preferences
    public static final String PREFERENCES_FILE = "preferences";
    public static final String DEFAULT_KEY_PREFERENCE = "default_key";
    //permissions
    public static final int CONTACT_PERMISSIONS_REQUEST=1;
    //Intent keys
    public static final String PASSPHRASE_INTENT_KEY ="passphrase";
    public static final String TEMP_PASSPHRASE_INTENT_KEY="temppassphrase";
    public static final String KEYID_INTENT_KEY="keyid";

    public static final String NAME_INTENT_KEY="name";
    public static final String EMAIL_INTENT_KEY="email";
    public static final String USERID_INTENT_KEY="userid";

    public static final String CONFIG_STATE="configd";
    public static final int CONFIG_STATE_NONE=0;
    public static final int CONFIG_STATE_KEYLOCK=1;
    public static final int CONFIG_STATE_CONFIGURED=2;
    public static final int DEFAULT_CONFIG_STATE=CONFIG_STATE_NONE;

    //timings in ms for the LearnPassprhase flashcards.
    public static final long FLASHWAIT = 800;
    public static final long FLASHPAUSE=  1200;
    public static final int FLASH_MINIMUM_REPETITIONS=3;
    public static final long RUBRICS_PAUSE=1500;

public static final String ASCII_ARMORED_EXTENSION="asc";


    public static final String PLAYSTORE_TWITTER_URL = "https://play.google.com/store/apps/details?id=com.twitter.android";
    static final float POPUP_PROPORTIONS=0.55f;
    static final float AD_POPUP_PROPORTIONS=0.95f;
   public static final int REQUEST_CAMERA_PERMISSION = 200;
    public static final String CREATE_KEYPAIR = "uk.co.platosys.keylocks.action.createkeypair";
    public static final String ACTION_NEW_KEY="uk.co.platosys.keylocks.action.newkey";
    public static final String ACTION_RESUME="uk.co.platosys.keylocks.action.resume";

}
