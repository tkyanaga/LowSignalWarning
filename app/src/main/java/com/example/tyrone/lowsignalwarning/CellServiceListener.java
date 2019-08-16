package com.example.tyrone.lowsignalwarning;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

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
        byte notifyBy = intent.getByteExtra(getString(R.string.signal_key), (byte)1);


        listener = new PhoneStateListener(){
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength){
                int signalLevel = signalStrength.getLevel();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


                if(goodService && signalLevel == CellSignalStrength.SIGNAL_STRENGTH_POOR){
                    Utils.showToast("Bad service detected", getApplicationContext());
                    Log.v(TAG, "Bad service detected");
                    goodService = false;
                    badServiceVibrate(v);
                    try{
                        Thread.sleep(delayTime);
                    }
                    catch(InterruptedException e){
                        Log.v(TAG, "Thread.sleep interrupted");
                    }
                }

                if(!goodService && signalLevel > CellSignalStrength.SIGNAL_STRENGTH_POOR){
                    goodService = true;
                    Utils.showToast("Good service detected", getApplicationContext());
                    Log.v(TAG, "Good service detected");
                    goodServiceVibrate(v);
                }
            }
        };

        telephonyManager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        return START_STICKY;
    }


    private void goodService(byte notifyBy, Vibrator v){
        switch(notifyBy){
            case 1:
                goodServiceVibrate(v);
                break;
            case 3:
                goodServiceVibrate(v);
            case 2:
                goodServiceLight();
                break;
            default:
                Log.v(TAG, "Good service function called");
                break;
        }
    }

    private void goodServiceLight(){
        blinkFlash(Utils.goodSignalPattern);
    }

    private void blinkFlash(long pattern[])
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        for (int i = 0; i < pattern.length; i++) {
            if (i % 2 == 0) {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, true);
                } catch (CameraAccessException e) {}
            }
            else {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, false);
                } catch (CameraAccessException e) {}
            }
            try {
                Thread.sleep(pattern[i]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




    private void badServiceVibrate(Vibrator v){

        if (v.hasVibrator()) {
            Log.v("Can Vibrate", "YES");
        } else {
            Log.v("Can Vibrate", "NO");
        }
        v.vibrate(Utils.goodSignalPattern, -1);
    }

    private void goodServiceVibrate(Vibrator v){

        if (v.hasVibrator()) {
            Log.v("Can Vibrate", "YES");
        } else {
            Log.v("Can Vibrate", "NO");
        }
        v.vibrate(Utils.badSignalPattern, -1);
    }

}