package uk.co.platosys.keylocks.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import uk.co.platosys.keylocks.R;
import uk.co.platosys.keylocks.services.LocksmithService;
import uk.co.platosys.keylocks.services.LockstoreService;


/**
 * All KeyLocks Activities extend this base class.
 *
 * Created by edward on 21/02/18.
 */

public abstract class BaseActivity extends FragmentActivity {
    int screenWidth;
    int screenHeight;
    boolean locksmithBinding=false;
    boolean lockstoreBinding=false;
    boolean bound = false;
    LocksmithService locksmithService;
    LockstoreService lockstoreService;
    String className = getClass().getName();
    AlertDialog alertDialog;
    Context context;

    private ServiceConnection lockStoreServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.e("BA", className + " is bound to the lockstore service");
            lockstoreBinding = true;
            lockstoreService=((LockstoreService.LockstoreBinder) iBinder).getService();
            onLockStoreBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("BA", className + " is unbound");
            lockstoreBinding = false;
            lockstoreService=null;
        }
    };

    private ServiceConnection lockSmithServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.i("BA", className + " is bound to the Locksmith service");
            locksmithBinding = true;
            locksmithService =((LocksmithService.LocksmithBinder) iBinder).getService();
            onLockSmithBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("BA", className + " is unbound");
            locksmithBinding = false;
            locksmithService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context=getApplicationContext();
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        Log.e("BA", "creating base activity ");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED){

        }
        bind();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!(bound)) {
            bind();
        }
    }

    @Override
    protected void onStop() {
        if(alertDialog!=null){alertDialog.dismiss();}
        if (bound) {
            unbindService(lockSmithServiceConnection);
            unbindService(lockStoreServiceConnection);
            bound = false;
        }
        super.onStop();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(alertDialog!=null){alertDialog.dismiss();}
    }

    private void bind() {
        Intent lockSmithServiceIntent = new Intent(this, LocksmithService.class);
        lockSmithServiceIntent.putExtra("activity", this.getClass().getName());
        Log.i("BA", className + " about to bind to locksmith service");
        bindService(lockSmithServiceIntent, lockSmithServiceConnection, Context.BIND_AUTO_CREATE);
        Intent lockStoreServiceIntent = new Intent(this, LockstoreService.class);
        lockStoreServiceIntent.putExtra("activity", this.getClass().getName());
        Log.e("BA", className + " about to bind to lockstore service");
        if(bindService(lockStoreServiceIntent, lockStoreServiceConnection, Context.BIND_AUTO_CREATE)) {
            bound = true;
        }
    }

    void showAlert(int title, int message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        alertDialog = alertDialogBuilder.create();
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
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(int title, String message, boolean cancelable, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        if (cancelable) {
            alertDialogBuilder.setNegativeButton(R.string.cancel, onClickListener);
        }
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(String title, int message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(String title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAlert(int title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(R.string.OK, onClickListener);
        alertDialog = alertDialogBuilder.create();
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
        if (bound) {
            Log.d("BA", this.getClass().getName() + " reporting bound");
        } else {
            Log.d("BA", this.getClass().getName() + " reporting not bound");
        }
    }
    protected abstract void onLockSmithBound();
    protected abstract void onLockStoreBound();
}
