package uk.co.platosys.keylocks.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.minigma.Algorithms;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.LockSmith;
import uk.co.platosys.minigma.LockStore;
import uk.co.platosys.minigma.Minigma;
import uk.co.platosys.minigma.MinigmaLockStore;
import uk.co.platosys.minigma.exceptions.Exceptions;


import static uk.co.platosys.keylocks.Constants.LOCKSTORE_FILE_NAME;
import static uk.co.platosys.keylocks.Constants.PASSPHRASE_INTENT_KEY;
import static uk.co.platosys.keylocks.Constants.TEMP_PASSPHRASE_INTENT_KEY;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>

 * helper methods.
 */
public class LocksmithService extends IntentService {
    private final IBinder iBinder = new LocksmithBinder();
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    //private static final String ACTION_BAZ = "uk.co.platosys.fingerprinter.action.BAZ";
    private static String TAG = "Locksmith Service";
      private LockStore lockstore;
    Context context;
    private List<OnKeyCreatedListener> keyCreatedListeners = new ArrayList<>();
    public LocksmithService() {
        super("LocksmithService");
        /*try {
            File lockstoreFile = new File(getFilesDir(), LOCKSTORE_FILE_NAME);
            this.lockstore = new MinigmaLockStore(lockstoreFile, true);
        }catch(Exception x){
            Exceptions.dump(x);
        }*/

    }

     /**
     * Starts this service to create a KeyPair. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startCreateKeyPair(Context context, char[] passphrase) {
     //   Log.e(TAG, "starting to create keypair for "+userID);
        Log.e(TAG, "using Minigmand version "+ Minigma.VERSION);
        Intent intent = new Intent(context, LocksmithService.class);
        intent.setAction(Constants.CREATE_KEYPAIR);
        //intent.putExtra(USERID_INTENT_KEY, userID);
        intent.putExtra(PASSPHRASE_INTENT_KEY, passphrase);
        try {
            context.startService(intent);
        }catch (Exception x){
            Log.e(TAG, "context exception ", x);
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "starting to handle intent");
        if (intent != null) {
            try {
                File lockstoreFile = new File(getFilesDir(), LOCKSTORE_FILE_NAME);
                this.lockstore = new MinigmaLockStore(lockstoreFile, true);
            }catch(Exception x){
                Exceptions.dump(x);
            }
            File keysDirectory = new File(getFilesDir(), Constants.KEYS_DIRECTORY_NAME);
            if (! keysDirectory.exists()){keysDirectory.mkdir();}
            try {

            }catch(Exception x){
                Exceptions.dump(x);
            }
            final String action = intent.getAction();
            if (action.equals(Constants.CREATE_KEYPAIR)) {
                //final String userID = intent.getStringExtra(USERID_INTENT_KEY);
                final char[] passphrase = intent.getCharArrayExtra(TEMP_PASSPHRASE_INTENT_KEY);
                intent.removeExtra(TEMP_PASSPHRASE_INTENT_KEY);
                //Log.e(TAG, "creating lockset for "+userID);
                try {
                    Log.e(TAG, "now creating the lockset");

                    Lock lock = LockSmith.createLockset(keysDirectory, lockstore, passphrase, Algorithms.RSA);
                    Log.e(TAG, "lockset created with shortID "+lock.getShortID());

                    Log.d(TAG, keysDirectory.getPath());
                    File keyfile = new File (keysDirectory, lock.getShortID());
                    if(keyfile.exists()){
                        Log.e(TAG, "keyfile created with path "+keyfile.getPath());
                    }else{
                        Log.e(TAG, "keyfile not created?");
                    }
                    Log.e(TAG, "there are "+keyCreatedListeners.size()+" okcListeners, about to notify them");
                    for(OnKeyCreatedListener onKeyCreatedListener:keyCreatedListeners){
                        onKeyCreatedListener.onKeyCreated(lock.getLockID());
                    }

                }catch (Exception x){
                    Exceptions.dump(x);
                }
            } else {
                Log.d (TAG, "wrong action in intent");
            }
        }
    }
    public void addOnKeyCreatedListener(OnKeyCreatedListener onKeyCreatedListener){
        Log.e(TAG, "adding an okcListener");
        keyCreatedListeners.add(onKeyCreatedListener);
        Log.e(TAG, "there are now "+keyCreatedListeners.size()+" okcListeners");
    }

    public interface OnKeyCreatedListener {
        void onKeyCreated(long keyID);
    }
    @Override
    public IBinder onBind(Intent intent){
        return iBinder;
    }

    public class LocksmithBinder extends Binder {
        public LocksmithService getService(){
            return LocksmithService.this;
        }
    }
}
