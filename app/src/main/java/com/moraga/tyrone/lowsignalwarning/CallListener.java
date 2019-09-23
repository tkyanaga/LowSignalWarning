package com.moraga.tyrone.lowsignalwarning;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallListener extends Service {
    public CallListener() {
    }

    private TelephonyManager telephonyManager;
    private PhoneStateListener listener;
    private String TAG = "CallListener";

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Create a new PhoneStateListener
        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (Utils.isOnCall) {
                            Log.v(TAG, "idle");
                            Utils.isOnCall = false;
                            stopService(new Intent(getBaseContext(), ServiceListener.class));
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.v(TAG, "offhook");
                        Utils.isOnCall = true;
                        Intent intent2 = new Intent(getBaseContext(), ServiceListener.class);
                        intent2.putExtra("Signal Key", intent.getByteExtra("Signal Key", (byte)0));
                        intent2.putExtra("Service Threshold", intent.getIntExtra("Service Threshold",0));
                        startService(intent2);

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