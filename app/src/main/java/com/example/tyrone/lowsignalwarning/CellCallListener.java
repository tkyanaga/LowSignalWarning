package com.example.tyrone.lowsignalwarning;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CellCallListener extends Service {
    public CellCallListener() {
    }

    private TelephonyManager telephonyManager;
    private PhoneStateListener listener;
    private boolean isOnCall;
    private String TAG = "CellCallListener";

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
                            Log.v(TAG, "idle");
                            isOnCall = false;
                            stopService(new Intent(getBaseContext(), CellServiceListener.class));
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.v(TAG, "offhook");
                        isOnCall = true;
                        startService(new Intent(getBaseContext(), CellServiceListener.class));

                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.v(TAG, "ringing");
                        break;
                }
            }
        };

        // Register the listener with the telephony manager
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy called");
    }
}