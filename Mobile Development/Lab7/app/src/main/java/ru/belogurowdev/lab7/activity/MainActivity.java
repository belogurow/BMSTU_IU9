package ru.belogurowdev.lab7.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.belogurowdev.lab7.R;
import ru.belogurowdev.lab7.adapter.PlacesAdapter;
import ru.belogurowdev.lab7.api.GooglePlacesApi;
import ru.belogurowdev.lab7.model.PlacesList;
import ru.belogurowdev.lab7.util.App;

public class MainActivity extends AppCompatActivity {

    private final static String API_KEY = "AIzaSyAuJIEnY4TcR-G67YJSgS2CNbPJNABzs3s";
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private EditText mEditTextQuery;
    private PlacesAdapter mAdapter;

    private GooglePlacesApi mGooglePlacesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();
    }

    private void initializeFields() {
        mGooglePlacesApi = App.mGooglePlacesApi;
        mProgressBar = findViewById(R.id.progress);
        mRecyclerView = findViewById(R.id.recycler);
        mEditTextQuery = findViewById(R.id.edit_query_places);

        mAdapter = new PlacesAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    private void sendRequest() {
        showLoad();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = sharedPreferences.getString(getString(R.string.pref_list_key_lang), "en");

        Observable<PlacesList> placesListObservable = mGooglePlacesApi.
                getPlacesBySearch(mEditTextQuery.getText().toString(), API_KEY, lang);
        placesListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        places -> {
                            hideLoad();
                            mAdapter.setPlacesList(places.getResults());
                            Log.d(TAG, places.toString());
                        },
                        error -> {
                            hideLoad();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                );

    }

    private void hideLoad() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showLoad() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_send_request:
                sendRequest();
                return true;
            case R.id.menu_action_open_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
