package ru.belogurowdev.lab7.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.belogurowdev.lab7.R;
import ru.belogurowdev.lab7.api.GooglePlacesApi;
import ru.belogurowdev.lab7.model.placeInfo.PlaceInfo;
import ru.belogurowdev.lab7.model.placeInfo.Result;
import ru.belogurowdev.lab7.model.placesList.PlacesList;
import ru.belogurowdev.lab7.util.App;

public class PlaceInfoActivity extends AppCompatActivity {

    private static final String TAG = PlaceInfoActivity.class.getSimpleName();
    private static final String EXTRA_PLACE_ID = "ru.belogurowdev.extras.PLACE_ID";
    private final static String API_KEY = "AIzaSyAuJIEnY4TcR-G67YJSgS2CNbPJNABzs3s";

    private TextView mTextPlaceName;
    private ProgressBar mProgressBar;
    private ImageView mImagePlacePhoto;

    private ImageView mIconCall;
    private ImageView mIconWeb;
    private ImageView mIconMap;

    private GooglePlacesApi mGooglePlacesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        initializeFields();
        sendRequest();
    }

    private void initializeListeners(Result result) {
        if (result.getInternationalPhoneNumber() != null) {
            mIconCall.setOnClickListener(view -> {
                String uri = String.format("tel:%s", result.getInternationalPhoneNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(callIntent);
            });
        } else {
            mIconCall.setVisibility(View.GONE);
        }

        if (result.getWebsite() != null) {
            mIconWeb.setOnClickListener(view -> {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getWebsite()));
                startActivity(webIntent);
            });
        } else {
            mIconWeb.setVisibility(View.GONE);
        }

        if (result.getGeometry().getLocation() != null) {
            mIconMap.setOnClickListener(view -> {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(webIntent);
            });
        } else {
            mIconMap.setVisibility(View.GONE);
        }


    }

    private void initializeFields() {
        mGooglePlacesApi = App.mGooglePlacesApi;
        mTextPlaceName = findViewById(R.id.place_info_name);
        mProgressBar = findViewById(R.id.progress_place_info);
        mImagePlacePhoto = findViewById(R.id.place_info_photo);

        mIconCall = findViewById(R.id.icon_call);
        mIconMap = findViewById(R.id.icon_map);
        mIconWeb = findViewById(R.id.icon_web);
    }

    private void sendRequest() {
        showLoad();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = sharedPreferences.getString(getString(R.string.pref_list_key_lang), "en");
        String placeId = getIntent().getStringExtra(EXTRA_PLACE_ID);

        Observable<PlaceInfo> placeInfoObservable = mGooglePlacesApi
                .getPlaceById(placeId, API_KEY, lang);
        placeInfoObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        info -> {
                            hideLoad();
                            setData(info.getResult());
                            initializeListeners(info.getResult());
                        },
                        error -> {
                            hideLoad();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                );
    }

    private void setData(Result result) {
        mTextPlaceName.setText(result.getName());

        if (result.getPhotos() == null) {
            mImagePlacePhoto.setVisibility(View.GONE);
        } else {
            Uri photoUri = Uri.parse("https://maps.googleapis.com")
                    .buildUpon()
                    .appendEncodedPath("maps")
                    .appendEncodedPath("api")
                    .appendEncodedPath("place")
                    .appendEncodedPath("photo")
                    .appendQueryParameter("maxwidth", "400")
                    .appendQueryParameter("photoreference", result.getPhotos().get(0).getPhotoReference())
                    .appendQueryParameter("key", API_KEY)
                    .build();
            Glide.with(this)
                    .load(photoUri)
                    .into(mImagePlacePhoto);
        }
    }

    private void hideLoad() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showLoad() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Finish activity when press back button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
