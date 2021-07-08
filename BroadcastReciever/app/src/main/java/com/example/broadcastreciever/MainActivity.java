package com.example.broadcastreciever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
TextView tv;
Battery br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.tv2);

        br=new Battery(tv);
        registerReceiver(br,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(br);
    }
}