package uk.co.platosys.keylocks.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

import uk.co.platosys.keylocks.Constants;
import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.LockStore;
import uk.co.platosys.minigma.MinigmaLockStore;
import uk.co.platosys.minigma.exceptions.LockNotFoundException;
import uk.co.platosys.minigma.exceptions.MinigmaException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LockstoreService extends IntentService {
    private final IBinder iBinder = new LockstoreBinder();
    private static final String TAG = "LStIS";
    private static final String ACTION_ADD_LOCK = "uk.co.platosys.keylocks.services.action.add_lock";
    private static final String ACTION_GET_LOCK = "uk.co.platosys.keylocks.services.action.get_lock";

    // TODO: Rename parameters
    private static final String EXTRA_FINGERPRINT = "uk.co.platosys.keylocks.services.extra.FINGERPRINT";
    private static final String EXTRA_LOCK = "uk.co.platosys.keylocks.services.extra.LOCK";
    LockStore lockstore;
    Context context;
    public LockstoreService() {
        super("LockstoreService");

    }
    public class LockstoreBinder extends Binder {
        public LockstoreService getService(){
            return LockstoreService.this;
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.e(TAG, action);
            switch(action){
                case ACTION_GET_LOCK:
                    final byte[] fingerprintBytes= intent.getByteArrayExtra(EXTRA_FINGERPRINT);
                    handleActionGetLock(fingerprintBytes);
                    break;
                case ACTION_ADD_LOCK:
                    final byte[] lockBytes = (intent.getByteArrayExtra(EXTRA_LOCK));
                    handleActionAddLock(lockBytes);
                    break;
                default:
                    Log.e(TAG, "Unsupported operation "+action);
                    throw new UnsupportedOperationException("unknown intent action");
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetLock(byte[] fingerprintBytes) {
        try {
            Lock lock = lockstore.getLock(new Fingerprint(fingerprintBytes));
        }catch(LockNotFoundException lnfe){

        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAddLock(byte[] lockBytes) {
       //Lock lock = new Lock(lockBytes);
       //lockstore.addLock(lock);
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public Lock getLock(Fingerprint fingerprint){
        try {
            return lockstore.getLock(fingerprint);
        }catch (LockNotFoundException lnfe){
            return null;
        }
    }
    public void initialise(Context context){
        this.context=context;
        try {
            /*note here we are using the MinigmaLockStore implementation, which uses OpenPGP-format PublicKeyRings,
            saved as Ascii-Armored text files.  As and when a better Android lockstore implementation (using a Room db) is ready,
            it should be used instead.

             */
            this.lockstore = new MinigmaLockStore(new File(getFilesDir(), Constants.LOCKSTORE_FILE_NAME), false);
        }catch (MinigmaException mx){

        }
    }
    @Override
    public  IBinder onBind(Intent intent){
        return iBinder;
    }
}
