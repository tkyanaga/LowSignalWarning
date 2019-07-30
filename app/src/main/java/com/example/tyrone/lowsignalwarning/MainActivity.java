package com.example.tyrone.lowsignalwarning;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    }

    private void stopWarning(){
        Toast.makeText(getApplicationContext(),"stopping warning",Toast.LENGTH_SHORT).show();
    }


}
