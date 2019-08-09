package com.example.tyrone.lowsignalwarning;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tyrone on 8/1/19.
 */

public class CellServiceListener extends IntentService {
    TelephonyManager tM;
    BroadcastReceiver bR;
    IntentFilter iF;


    public CellServiceListener(){
        super("CellServiceListener");
    }


    protected void onHandleIntent(Intent intent){
        //what the service does
        Log.v("onHandleIntent", "Pre tm");
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        PhoneStateListener callStateListener = new PhoneStateListener(){
            public void onCallStateChanged(int state, String incomingNumber){
                if (state == TelephonyManager.CALL_STATE_OFFHOOK){
                    Log.v("Call State", "offHook");
                }
            }
        };
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        String state = Integer.toString(telephonyManager.getCallState());
        Log.v("telephony manager says", state);
//        iF = new IntentFilter();
//        iF.addAction("android.intent.action.PHONE_STATE");
//        bR = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle bundle = intent.getExtras();
//                if(bundle == null) {
//                    return;
//                }
//
//                //Outgoing
//                String outState = bundle.getString(TelephonyManager.EXTRA_STATE);
//                if(outState != null && outState == TelephonyManager.EXTRA_STATE_OFFHOOK){
//                    Log.v("Outgoing call state", "offHook");
//                }
//            }
//        };
//
//        registerReceiver(bR, iF);
        Log.v("onHandleIntent", "Post tm");
    }

    @Override
    public void onDestroy(){
        Log.v("onDestroy", "called");
//        unregisterReceiver(bR);
    }
}
