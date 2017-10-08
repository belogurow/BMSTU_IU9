package ru.belogurowdev.lab6.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.belogurowdev.lab6.R;
import ru.belogurowdev.lab6.adapter.WeatherAdapter;
import ru.belogurowdev.lab6.api.WeatherApi;
import ru.belogurowdev.lab6.model.WeatherData;
import ru.belogurowdev.lab6.util.App;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY = "3cb09e739fef1e349def2c8634be954a";
    private static final String CELSIUS = "Metric";

    private RecyclerView mRecyclerViewWeather;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private WeatherAdapter mAdapter;

    private WeatherApi mWeatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();

        Observable<WeatherData> weatherData = mWeatherApi.getWeatherByCity("Moscow", "16", KEY, CELSIUS);
        weatherData
                .subscribeOn(Schedulers.io())                 // Subscribe выполняется в отдельном потоке
                .observeOn(AndroidSchedulers.mainThread())    // Observe выполняется в главном потоке
                .subscribe(
                        weather -> {
                            hideLoad();
                            mTextView.setText(weather.getCity().getName());
                            mAdapter.setWeatherList(weather.getList());
                        },
                        error   -> {
                            hideLoad();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );

        Log.d(TAG, "end");
    }

    private void initializeFields() {
        mWeatherApi = App.mWeatherApi;
        mAdapter = new WeatherAdapter(this);
        mProgressBar = findViewById(R.id.progress);
        mTextView = findViewById(R.id.text_city_name);

        mRecyclerViewWeather = findViewById(R.id.recycler_weather);
        mRecyclerViewWeather.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewWeather.setAdapter(mAdapter);
        mRecyclerViewWeather.setHasFixedSize(true);
    }

    private void showLoad() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoad() {
        mProgressBar.setVisibility(View.GONE);
    }
}
