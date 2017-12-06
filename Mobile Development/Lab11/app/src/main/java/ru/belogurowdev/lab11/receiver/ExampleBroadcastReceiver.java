package ru.belogurowdev.lab11.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import ru.belogurowdev.lab11.service.ExampleService;

/**
 * Created by alexbelogurow on 06.12.2017.
 */

public class ExampleBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = ExampleBroadcastReceiver.class.getSimpleName();

    public ExampleBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "RECEIVE_EVENT", Toast.LENGTH_SHORT).show();

        String message;
        if (intent != null && intent.getStringExtra("message") != null) {
            message = intent.getStringExtra("message");
            Log.d(TAG, message);
        } else {
            Log.d(TAG, "message is null");
        }
    }


}
