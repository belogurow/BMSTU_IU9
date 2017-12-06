package ru.belogurowdev.lab11.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ru.belogurowdev.lab11.R;
import ru.belogurowdev.lab11.receiver.ExampleBroadcastReceiver;
import ru.belogurowdev.lab11.service.ExampleService;

public class MainActivity extends AppCompatActivity {

    private Intent mIntentService;
    private BroadcastReceiver mExampleReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentService = new Intent(this, ExampleService.class);
        mIntentService.putExtra("message", "Hello from MainActivity.class");
        startService(mIntentService);


        mExampleReceiver = new ExampleBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction("ru.belogurowdev.lab11.receiver.HELLO_LAB11");
        registerReceiver(mExampleReceiver, intentFilter);
    }

    public void broadcastOnClick(View view) {
        Intent intentReceiver = new Intent();
        intentReceiver.setAction("ru.belogurowdev.lab11.receiver.HELLO_LAB11");
        intentReceiver.putExtra("message", "Hello from MainActivity.class");
        sendBroadcast(intentReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mExampleReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(mIntentService);
    }
}
