package com.example.tyrone.lowsignalwarning;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Tyrone on 8/1/19.
 */

public class CellServiceListener extends IntentService {

    public CellServiceListener(){
        super("CellServiceListener");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        PhoneStateListener callStateListener = new PhoneStateListener(){
            public void onCallStateChanged(int state, String incomingNumber){
                if (state == TelephonyManager.CALL_STATE_OFFHOOK){
                    Log.v("Call State", "offHook");
                }
            }
        };
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    protected void onHandleIntent(Intent intent){
        //what the service does
//        Log.v("onHandleIntent", "Pre tm");
//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//        PhoneStateListener callStateListener = new PhoneStateListener(){
//            public void onCallStateChanged(int state, String incomingNumber){
//                if (state == TelephonyManager.CALL_STATE_OFFHOOK){
//                    Log.v("Call State", "offHook");
//                }
//            }
//        };
//        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
//        String state = Integer.toString(telephonyManager.getCallState());
//        Log.v("telephony manager says", state);
        Log.v("onHandleIntent", "Post tm");
    }
}
