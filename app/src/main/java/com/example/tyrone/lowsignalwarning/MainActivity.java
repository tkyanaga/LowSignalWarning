package com.example.tyrone.lowsignalwarning;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button startButton;
    private Button stopButton;
    private int service_level_int = 2; //default, 2, will notify below moderate service
    private static final String TAG = "Main Activity";

    //TODO  pass a signal of string so it can be retrieved
    enum signalType{
        VIBRATE, LIGHT, BOTH
    }

    private signalType notifyBy = signalType.BOTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_service);
        startButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop_service);
        stopButton.setOnClickListener(this);


        Spinner spinner = (Spinner) findViewById(R.id.notify_level_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.signal_level, android.R.layout.simple_spinner_item);
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
        intent.putExtra(Globals.signalKey, notifyBy);
        intent.putExtra(Globals.serviceThreshold, service_level_int);
        startService(intent);
    }

    private void stopWarning(){
        Toast.makeText(getApplicationContext(),"stopping warning",Toast.LENGTH_SHORT).show();
        stopService(new Intent(this, CellCallListener.class));
    }
}
