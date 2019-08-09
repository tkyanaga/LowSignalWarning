package com.example.tyrone.lowsignalwarning;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CellCallListener extends Service {
    public CellCallListener() {
    }

    private TelephonyManager telephonyManager;
    private PhoneStateListener listener;
    private boolean isOnCall;
    private String TAG = "Phone State";

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isOnCall = false;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a new PhoneStateListener
        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (isOnCall) {
                            showToast("Call state: idle");
                            Log.v(TAG, "idle");
                            isOnCall = false;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        showToast("Call state: offhook");
                        Log.v(TAG, "offhook");
                        isOnCall = true;
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        showToast("call state: ringing");
                        Log.v(TAG, "ringing");
                        break;
                }
            }
        };

        // Register the listener with the telephony manager
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
    }
}