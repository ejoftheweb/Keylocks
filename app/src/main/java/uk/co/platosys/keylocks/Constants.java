package uk.co.platosys.keylocks;

import java.io.File;

/**
 * Created by edward on 13/02/18.
 */

public class Constants {
    public static final String HKP_PROTOCOL = "https:";
    public static final String HKP_HOST ="keyserver.ubuntu.com";
    public static final int HKP_PORT=11371;
    public static final String HKP_FILE="";

    public static final String PGP_PUBRING_FILENAME = "pubring.asc";
    public static final String PGP_SECRING_FILENAME= "secring.asc";
    public static final String PGP_PUBRING_FOLDER="PGPKeys";

    //
    public static final String KEYS_DIRECTORY_NAME="farina";
    public static final String LOCKSTORE_FILE_NAME="lockstore";



    public static final File PGP_KEYRING_FILE=new File ("pgpkeyringfile");//TODO

    public static final String PREFS_USERNAME_KEY="username";

    public static final int MIN_PASSPHRASE_LENGTH=1;//for production, increase to 4
    public static final int DEFAULT_PASSPHRASE_LENGTH=7;
    //preferences
    public static final String PREFERENCES_FILE = "preferences";
    public static final String DEFAULT_KEY_PREFERENCE = "default_key";

    //Intent keys
    public static final String PASSPHRASE_INTENT_KEY ="passphrase";
    public static final String TEMP_PASSPHRASE_INTENT_KEY="temppassphrase";
    public static final String NAME_INTENT_KEY="name";
    public static final String EMAIL_INTENT_KEY="email";
    public static final String USERID_INTENT_KEY="userid";
    public static final String TAPPID_INTENT_KEY="tappid";
    public static final String BROADCAST_MESSAGE="broadcastmessage";
    //timings in ms for the LearnPassprhase flashcards.
    public static final long FLASHWAIT = 800;
    public static final long FLASHPAUSE=  1200;
    public static final long RUBRICS_PAUSE=1500;

public static final String ASCII_ARMORED_EXTENSION="asc";


    public static final String PLAYSTORE_TWITTER_URL = "https://play.google.com/store/apps/details?id=com.twitter.android";
    static final float POPUP_PROPORTIONS=0.55f;
    static final float AD_POPUP_PROPORTIONS=0.95f;
   public static final int REQUEST_CAMERA_PERMISSION = 200;
}
