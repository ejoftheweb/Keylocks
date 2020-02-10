package uk.co.platosys.keylocks.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.File;

import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.LockStore;
import uk.co.platosys.minigma.MinigmaLockStore;
import uk.co.platosys.minigma.exceptions.Exceptions;

import static uk.co.platosys.keylocks.Constants.LOCKSTORE_FILE_NAME;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LockStoreIntentService extends IntentService {
    private static final String ACTION_ADD_LOCK = "uk.co.platosys.keylocks.services.action.add_lock";
    private static final String ACTION_GET_LOCK = "uk.co.platosys.keylocks.services.action.get_lock";

    // TODO: Rename parameters
    private static final String EXTRA_FINGERPRINT = "uk.co.platosys.keylocks.services.extra.FINGERPRINT";
    private static final String EXTRA_LOCK = "uk.co.platosys.keylocks.services.extra.LOCK";
    LockStore lockstore;
    public LockStoreIntentService() {
        super("LockStoreIntentService");
        try {
            File lockstoreFile = new File(getFilesDir(), LOCKSTORE_FILE_NAME);
            this.lockstore = new MinigmaLockStore(lockstoreFile, true);
        }catch(Exception x){
            Exceptions.dump(x);
        }
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
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
}
