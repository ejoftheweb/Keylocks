package uk.co.platosys.keylocks.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.LockStore;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LockstoreService extends IntentService {
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
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAddLock(byte[] lockBytes) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public Lock getLock(long keyID){
        return null;
        //return lockstore.getLock(keyID);
    }
}
