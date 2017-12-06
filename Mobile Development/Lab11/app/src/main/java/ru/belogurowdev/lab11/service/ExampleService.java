package ru.belogurowdev.lab11.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by alexbelogurow on 06.12.2017.
 */

public class ExampleService extends IntentService {

    private final static String TAG = ExampleService.class.getSimpleName();

    public ExampleService() {
        super("ExampleService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String message;
        if (intent != null && intent.getStringExtra("message") != null) {
            message = intent.getStringExtra("message");
            Log.d(TAG, message);
        } else {
            Log.d(TAG, "message is null");
        }

        try {
            while (true) {
                Log.d(TAG, "service is worked");
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");

        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d(TAG, "onStart()");

        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");

        super.onDestroy();
    }
}
