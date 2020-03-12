package uk.co.platosys.keylocks.models;


import android.provider.ContactsContract;

/**Class to manage addresses. This class doesn't store addresses itself but uses the Android
 * device Contacts database.
 * [TODO: implements LockStore]
 */
public class AddressBook {
private static final String [] PROJECTION= {
        ContactsContract.Data._ID,
        ContactsContract.Data.DISPLAY_NAME_PRIMARY,
        ContactsContract.Data.CONTACT_ID,
        ContactsContract.Data.LOOKUP_KEY
};
private static final String SELECTION=  ContactsContract.CommonDataKinds.Email.ADDRESS + " LIKE ? " + "AND " +
                   ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";

    String searchString;
    String[] selectionArgs = { "" };
    public AddressBook(){
        //get all the contacts:



    }


}
