package com.example.tyrone.lowsignalwarning;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button startButton;
    private Button stopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_service);
        startButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop_service);
        stopButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.start_service:
                startWarning();
                break;
            case R.id.stop_service:
                stopWarning();
                break;
        }

    }

    private void startWarning(){
        Toast.makeText(getApplicationContext(),"starting warning",Toast.LENGTH_SHORT).show();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener callStateListener = new PhoneStateListener(){
            public void onCallStateChanged(int state, String incomingNumber){
                if (state == TelephonyManager.CALL_STATE_OFFHOOK){
                    Log.v("Call State", "offHook");
                }
            }
        };
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void stopWarning(){
        Toast.makeText(getApplicationContext(),"stopping warning",Toast.LENGTH_SHORT).show();
        goodServiceVibrate();
    }



    private void serviceListener(){

    }



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
