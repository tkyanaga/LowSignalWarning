package com.example.tyrone.lowsignalwarning;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button startButton;
    private Button stopButton;
    private Switch lightSwitch;
    private Switch vibrateSwitch;


    private int service_level_int = 2; //default, 2, will notify below moderate service
    private static final String TAG = "Main Activity";


    //TODO why is this crashing the app now!?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        //Buttons & Switches setup
        startButton = (Button) findViewById(R.id.start_service);
        startButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop_service);
        stopButton.setOnClickListener(this);


        lightSwitch = (Switch) findViewById(R.id.lightSwitch);
        if(!hasCameraFlash){
            lightSwitch.setClickable(false);
            lightSwitch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.showToast("No flashlight found", getApplicationContext());
                    return event.getActionMasked() == MotionEvent.ACTION_MOVE;
                }
            });
        }
        vibrateSwitch  = (Switch) findViewById(R.id.vibrateSwitch);
        vibrateSwitch.setChecked(true);

        Spinner spinner = (Spinner) findViewById(R.id.notify_level_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.signal_level, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(service_level_int);//the default value in action

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // a level of service was chosen, pull the int out
                // for readability, this isn't done inside 'valueOf'
                String service_level_string = parent.getItemAtPosition(pos).toString();
                Log.v(TAG, "changed to" + service_level_string);

                try {
                    service_level_int = Integer.valueOf(service_level_string.charAt(0)) - 48; // char(0) == 48
                } catch (Exception e) {
                    Log.e(TAG, "Int not found in signal_level choice");
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
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
        Toast.makeText(getApplicationContext(),"starting warning: " + service_level_int,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CellCallListener.class);

        //default is vibrate only
        byte notifyBy = 1;
        if(!vibrateSwitch.isChecked()){
            Log.v(TAG, "vibrate off");
            notifyBy -= 1;
        }
        if(lightSwitch.isChecked()){
            Log.v(TAG, "light on");
            notifyBy += 2;
        }
        intent.putExtra(getString(R.string.signal_key), notifyBy);
        intent.putExtra(getString(R.string.service_threshold), service_level_int);
        startService(intent);
    }

    private void stopWarning(){
        Toast.makeText(getApplicationContext(),"stopping warning",Toast.LENGTH_SHORT).show();
        stopService(new Intent(this, CellCallListener.class));
    }
}
