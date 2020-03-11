package uk.co.platosys.keylocks.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.keylocks.models.Identity;
import uk.co.platosys.keylocks.models.IdentityModel;
/**
 * A collection of authentication and account connection utilities. With strong inspiration from the Google IO session
 * app.
 * @author Dandré Allison
 *//*nicked off of github*/

public class AccountUtils {

public static final String TAG="AUT";
        /**
         * Interface for interacting with the result of {@link AccountUtils#getUserProfile}.
         */
        public static class UserProfile {

            /**
             * Adds an email address to the list of possible email addresses for the user
             * @param email the possible email address
             */
            public void addPossibleEmail(String email) {
                addPossibleEmail(email, false);
            }

            /**
             * Adds an email address to the list of possible email addresses for the user. Retains information about whether this
             * email address is the primary email address of the user.
             * @param email the possible email address
             * @param is_primary whether the email address is the primary email address
             */
            public void addPossibleEmail(String email, boolean is_primary) {
                if (email == null) return;
                if (is_primary) {
                    _primary_email = email;
                    _possible_emails.add(email);
                } else
                    _possible_emails.add(email);
            }

            /**
             * Adds a name to the list of possible names for the user.
             * @param name the possible name
             */
            public void addPossibleName(String name) {
                if (name != null) _possible_names.add(name);
            }

            /**
             * Adds a phone number to the list of possible phone numbers for the user.
             * @param phone_number the possible phone number
             */
            public void addPossiblePhoneNumber(String phone_number) {
                if (phone_number != null) _possible_phone_numbers.add(phone_number);
            }

            /**
             * Adds a phone number to the list of possible phone numbers for the user.  Retains information about whether this
             * phone number is the primary phone number of the user.
             * @param phone_number the possible phone number
             * @param is_primary whether the phone number is teh primary phone number
             */
            public void addPossiblePhoneNumber(String phone_number, boolean is_primary) {
                if (phone_number == null) return;
                if (is_primary) {
                    _primary_phone_number = phone_number;
                    _possible_phone_numbers.add(phone_number);
                } else
                    _possible_phone_numbers.add(phone_number);
            }

            /**
             * Sets the possible photo for the user.
             * @param photo the possible photo
             */
            public void addPossiblePhoto(Uri photo) {
                if (photo != null) _possible_photo = photo;
            }

            /**
             * Retrieves the list of possible email addresses.
             * @return the list of possible email addresses
             */
            public List<String> possibleEmails() {
                return _possible_emails;
            }

            /**
             * Retrieves the list of possible names.
             * @return the list of possible names
             */
            public List<String> possibleNames() {
                return _possible_names;
            }

            /**
             * Retrieves the list of possible phone numbers
             * @return the list of possible phone numbers
             */
            public List<String> possiblePhoneNumbers() {
                return _possible_phone_numbers;
            }

            /**
             * Retrieves the possible photo.
             * @return the possible photo
             */
            public Uri possiblePhoto() {
                return _possible_photo;
            }

            /**
             * Retrieves the primary email address.
             * @return the primary email address
             */
            public String primaryEmail() {
                return _primary_email;
            }

            /**
             * Retrieves the primary phone number
             * @return the primary phone number
             */
            public String primaryPhoneNumber() {
                return _primary_phone_number;
            }
            public List<Identity> getIdentities(){
                return identities;
            }
            /** all the identies as as List **/
            private List<Identity> identities;
            /** The primary email address */
            private String _primary_email;
            /** The primary name */
            private String _primary_name;
            /** The primary phone number */
            private String _primary_phone_number;
            /** A list of possible email addresses for the user */
            private List<String> _possible_emails = new ArrayList<>();
            /** A list of possible names for the user */
            private List<String> _possible_names = new ArrayList<>();
            /** A list of possible phone numbers for the user */
            private List<String> _possible_phone_numbers = new ArrayList<>();
            /** A possible photo for the user */
            private Uri _possible_photo;
        }

        /**
         * Retrieves the user profile information.
         * @param context the context from which to retrieve the user profile
         * @return the user profile
         */
        public static UserProfile getUserProfile(Context context) {
            return getUserProfileOnIcsDevice(context);
        }

        /**
         * Retrieves the user profile information in a manner supported by Gingerbread devices.
         * @param context the context from which to retrieve the user's email address and name
         * @return a list of the possible user's email address and name
         */
       /* private static UserProfile getUserProfileOnGingerbreadDevice(Context context) {
            // Other that using Patterns (API level 8) this works on devices down to API level 5
            final Matcher valid_email_address = Patterns.EMAIL_ADDRESS.matcher("");
            final Account[] accounts = AccountManager.get(context).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            final UserProfile user_profile = new UserProfile();
            // As far as I can tell, there is no way to get the real name or phone number from the Google account
            for (Account account : accounts) {
                if (valid_email_address.reset(account.name).matches())
                    user_profile.addPossibleEmail(account.name);
            }
            // Gets the phone number of the device is the device has one
            if (context.getPackageManager().hasSystemFeature(TELEPHONY_SERVICE)) {
                final TelephonyManager telephony = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                user_profile.addPossiblePhoneNumber(telephony.getLine1Number());
            }

            return user_profile;
        }*/

        /**
         * Retrieves the user profile information in a manner supported by Ice Cream Sandwich devices.
         * @param context the context from which to retrieve the user's email address and name
         * @return  a list of the possible user's email address and name
         */

        private static UserProfile getUserProfileOnIcsDevice(Context context) {
            final ContentResolver content = context.getContentResolver();
            final Cursor cursor = content.query(
                    // Retrieves data rows for the device user's 'profile' contact
                    Uri.withAppendedPath(
                            ContactsContract.Profile.CONTENT_URI,
                            ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                    ProfileQuery.PROJECTION,

                    // Selects only email addresses or names
                    ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                            + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                            + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                            + ContactsContract.Contacts.Data.MIMETYPE + "=?",
                    new String[]{
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                    },

                    // Show primary rows first. Note that there won't be a primary email address if the
                    // user hasn't specified one.
                    ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
            );

            final UserProfile user_profile = new UserProfile();
            user_profile.identities = new ArrayList<>();
            String mime_type;
            String identity_text_value=null;
            String identity_type=null;
            String identity_type_description=null;
            while (cursor.moveToNext()) {
                mime_type = cursor.getString(ProfileQuery.MIME_TYPE);
                switch (mime_type) {
                    case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                        user_profile.addPossibleEmail(cursor.getString(ProfileQuery.EMAIL), cursor.getInt(ProfileQuery.IS_PRIMARY_EMAIL) > 0);
                        identity_text_value = cursor.getString(ProfileQuery.EMAIL);
                        identity_type = Identity.TYPE_EMAIL;
                        identity_type_description = Identity.TYPE_DESCRIPTION_EMAIL;
                        break;
                    case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                        user_profile.addPossibleName(cursor.getString(ProfileQuery.GIVEN_NAME) + " " + cursor.getString(ProfileQuery.FAMILY_NAME));
                        identity_text_value = cursor.getString(ProfileQuery.GIVEN_NAME) + " " + cursor.getString(ProfileQuery.FAMILY_NAME);
                        identity_type = Identity.TYPE_NAME;
                        identity_type_description = Identity.TYPE_DESCRIPTION_NAME;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                        user_profile.addPossiblePhoneNumber(cursor.getString(ProfileQuery.PHONE_NUMBER), cursor.getInt(ProfileQuery.IS_PRIMARY_PHONE_NUMBER) > 0);
                        identity_text_value = cursor.getString(ProfileQuery.PHONE_NUMBER);
                        identity_type = Identity.TYPE_PHONE;
                        identity_type_description = Identity.TYPE_DESCRIPTION_PHONE;
                        break;
                    case ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE:

                        //user_profile.addPossiblePhoto(Uri.parse(cursor.getString(ProfileQuery.PHOTO)));
                    default:
                }
                IdentityModel im = new IdentityModel(identity_text_value,identity_type,identity_type_description);
                Log.d(TAG, identity_text_value+" "+identity_type+" "+identity_type_description);
                user_profile.identities.add(im);

                Log.d(TAG, user_profile.identities.size()+"identities");

            }
            cursor.close();
            return user_profile;
        }
    /* for (Account account : AccountManager.get(this).getAccounts()) {
            Identity identity = new IdentityModel(account.name, account.type, null);
            identities.add(identity);
        }*/
/*
        /*do some more to get the phone number here*/
        /*String[] projection = new String[] {
                ContactsContract.Profile._ID,
                ContactsContract.Profile.IS_USER_PROFILE,
                ContactsContract.Profile.LOOKUP_KEY
        };
        Cursor profileCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, null,null,null);
        profileCursor.moveToFirst();
        while(!(profileCursor.isLast())) {
            String userDisplayName = profileCursor.getString(profileCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DATA1));
            String
        }*/
        /**
         * Contacts user profile query interface.
         */
        private interface ProfileQuery {
            /** The set of columns to extract from the profile query results */
            String[] PROJECTION = {
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
                    ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                    ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
                    ContactsContract.CommonDataKinds.Photo.PHOTO_URI,
                    ContactsContract.Contacts.Data.MIMETYPE
            };

            /** Column index for the email address in the profile query results */
            int EMAIL = 0;
            /** Column index for the primary email address indicator in the profile query results */
            int IS_PRIMARY_EMAIL = 1;
            /** Column index for the family name in the profile query results */
            int FAMILY_NAME = 2;
            /** Column index for the given name in the profile query results */
            int GIVEN_NAME = 3;
            /** Column index for the phone number in the profile query results */
            int PHONE_NUMBER = 4;
            /** Column index for the primary phone number in the profile query results */
            int IS_PRIMARY_PHONE_NUMBER = 5;
            /** Column index for the photo in the profile query results */
            int PHOTO = 6;
            /** Column index for the MIME type in the profile query results */
            int MIME_TYPE = 7;
        }
}

