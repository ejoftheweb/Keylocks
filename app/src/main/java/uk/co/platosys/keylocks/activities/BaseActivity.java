package uk.co.platosys.keylocks.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LockStoreIntentService;
import uk.co.platosys.keylocks.services.LocksmithService;


/**
 * All passphrases activities extend this base class.
 *
 * Created by edward on 21/02/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    int screenWidth;
    int screenHeight;
    boolean bound = false;
    boolean binding = false;
    LocksmithService locksmithService;
    String className = getClass().getName();
    List<OnLockSmithServiceBoundListener> onLockSmithServiceBoundListenerList = new ArrayList<>();
    List<OnLockStoreServiceBoundListener> onLockStoreServiceBoundListenerList = new ArrayList<>();

    private ServiceConnection lockStoreServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.d("BA", className + " is bound");
            binding = true;
            for (OnLockStoreServiceBoundListener onLockStoreServiceBoundListener : onLockStoreServiceBoundListenerList) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("BA", className + " is unbound");
            binding = false;
        }
    };
    private ServiceConnection lockSmithServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.d("BA", className + " is bound");
            binding = true;
            for (OnLockSmithServiceBoundListener onLockSmithServiceBoundListener : onLockSmithServiceBoundListenerList) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("BA", className + " is unbound");
            binding = false;
        }
    };

    public interface OnLockStoreServiceBoundListener {

    }

    public interface OnLockSmithServiceBoundListener {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        bind();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bound) {
            bind();
        }

    }

    @Override
    protected void onStop() {
        if (bound) {
            unbindService(lockSmithServiceConnection);
            unbindService(lockStoreServiceConnection);
            bound = false;
        }
        super.onStop();

    }

    private void bind() {
        Intent lockSmithServiceIntent = new Intent(this, LocksmithService.class);
        lockSmithServiceIntent.putExtra("activity", this.getClass().getName());
        Log.i("BA", className + " about to bind to service");
        bindService(lockSmithServiceIntent, lockSmithServiceConnection, Context.BIND_AUTO_CREATE);
        Intent lockStoreServiceIntent = new Intent(this, LockStoreIntentService.class);
        lockStoreServiceIntent.putExtra("activity", this.getClass().getName());
        Log.i("BA", className + " about to bind to service");
        bindService(lockStoreServiceIntent, lockStoreServiceConnection, Context.BIND_AUTO_CREATE);
        bound = true;
    }


    void showAlert(int title, int message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(int title, int message, boolean cancelable, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        if (cancelable) {
            alertDialogBuilder.setNegativeButton(R.string.cancel, onClickListener);
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(String title, int message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(String title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(int title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void reportBinding() {
        if (binding) {
            Log.d("BA", this.getClass().getName() + " reporting bound");
        } else {
            Log.d("BA", this.getClass().getName() + " reporting not bound");
        }
    }

    public void addOnLockSmithServiceBoundListener(OnLockSmithServiceBoundListener onServiceBoundListener) {
        onLockSmithServiceBoundListenerList.add(onServiceBoundListener);
        Log.d("BA", "OLSmSBL added, now " + onLockSmithServiceBoundListenerList.size() + " listeners");
    }

    public void addOnLockStoreServiceBoundListener(OnLockStoreServiceBoundListener onServiceBoundListener) {
        onLockStoreServiceBoundListenerList.add(onServiceBoundListener);
        Log.d("BA", "OLStSBL added, now " + onLockStoreServiceBoundListenerList.size() + " listeners");
    }
}
