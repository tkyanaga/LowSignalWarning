package com.example.tyrone.lowsignalwarning;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tyrone on 8/1/19.
 */

public class CellServiceListener extends Service{
    public CellServiceListener(){

    }
    private TelephonyManager telephonyManager;
    private PhoneStateListener listener;
    private int delayTime = 5000;
    private boolean goodService = true;
    private String TAG = "CellServiceListener";

    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.v(TAG, "Second service started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
//        MainActivity.signalType notifyBy = intent.getExtras(Globals.signalKey);
//        TODO
        listener = new PhoneStateListener(){
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength){
                int signalLevel = signalStrength.getLevel();

                if(goodService && signalLevel == CellSignalStrength.SIGNAL_STRENGTH_POOR){
                    showToast("Bad service detected");
                    Log.v(TAG, "Bad service detected");
                    goodService = false;
                    badServiceVibrate();
                    try{
                        Thread.sleep(delayTime);
                    }
                    catch(InterruptedException e){
                        Log.v(TAG, "Thread.sleep interrupted");
                    }
                }

                if(!goodService && signalLevel > CellSignalStrength.SIGNAL_STRENGTH_POOR){
                    goodService = true;
                    showToast("Good service detected");
                    Log.v(TAG, "Good service detected");
                    goodServiceVibrate();
                }
            }
        };

        telephonyManager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        return START_STICKY;
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
//TODO
//    private void goodService(MainActivity.signalType notifyBy){
//        switch(notifyBy){
//            case VIBRATE:
//                goodServiceVibrate();
//                break;
//            case BOTH:
//                goodServiceVibrate();
//            case LIGHT:
//                goodServiceLight();
//                break;
//        }
//    }


    private void badServiceVibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (v.hasVibrator()) {
            Log.v("Can Vibrate", "YES");
        } else {
            Log.v("Can Vibrate", "NO");
        }
        long badSignalPattern[] = {0, 150, 30, 150, 30, 150, 30, 150};
        v.vibrate(badSignalPattern, -1);
    }

    private void goodServiceVibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (v.hasVibrator()) {
            Log.v("Can Vibrate", "YES");
        } else {
            Log.v("Can Vibrate", "NO");
        }
        long badSignalPattern[] = {0, 300, 100, 50};
        v.vibrate(badSignalPattern, -1);
    }

}