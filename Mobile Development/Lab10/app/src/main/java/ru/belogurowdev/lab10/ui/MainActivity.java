package ru.belogurowdev.lab10.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import ru.belogurowdev.lab10.R;
import ru.belogurowdev.lab10.adapter.EmployeeAdapter;
import ru.belogurowdev.lab10.dao.EmployeeDao;
import ru.belogurowdev.lab10.database.EmployeeDatabase;
import ru.belogurowdev.lab10.model.Employee;
import ru.belogurowdev.lab10.provider.SampleContentProvider;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_EMPLOYEE = 1;

    private RecyclerView mRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private EmployeeDao mEmployeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        test();

        loadData();

    }

    private void initViews() {
        mEmployeeDao = EmployeeDatabase.getInstance(getApplicationContext()).getEmployeeDao();

        mEmployeeAdapter = new EmployeeAdapter(this);

        mRecyclerView = findViewById(R.id.recycler_employers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mEmployeeAdapter);



    }

    private void loadData() {
//        mEmployeeDao.deleteAll();
//        mEmployeeDao.insert(new Employee("Max", 20));
//        mEmployeeDao.insert(new Employee("Alex", 43));
//        mEmployeeDao.insert(new Employee("Tom", 34));
//        mEmployeeDao.insert(new Employee("Jane", 29));
//        mEmployeeDao.insert(new Employee("Phillip", 19));

        //getSupportLoaderManager().initLoader(LOADER_EMPLOYEE, null, mLoaderCallbacks);

        Cursor mCursor;
        mCursor = getContentResolver().query(
                Uri.parse(SampleContentProvider.URI_EMPLOYEE).buildUpon().build(),
                null,
                null,
                new String[] {Employee.COLUMN_FULL_NAME},
                null);

        if (mCursor != null) {
            mEmployeeAdapter.setEmployers(mCursor);
            //mCursor.close();
        }
    }

    /**
     * Testing Insert, Update and Delete
     */
    private void test() {
        ContentValues values = new ContentValues();
        values.put(Employee.COLUMN_ID, 8);
        values.put(Employee.COLUMN_FULL_NAME, "Robert");
        values.put(Employee.COLUMN_AGE, 34);

        // Insert
        Uri uri = getContentResolver().insert(
                Uri.parse(SampleContentProvider.URI_EMPLOYEE).buildUpon().build(),
                values);

        if (uri == null) {
            throw new IllegalArgumentException("URI is null");
        }

        // Update
        values.put(Employee.COLUMN_AGE, 40);
        getContentResolver().update(
                uri,
                values,
                null,
                null);

        // Delete
        getContentResolver().delete(
                uri,
                null,
                null);
    }

    /**
     * For example
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    switch (id) {
                        case LOADER_EMPLOYEE:
                            return new CursorLoader(getApplicationContext(),
                                    Uri.parse(SampleContentProvider.URI_EMPLOYEE).buildUpon().build(),                                    new String[] {Employee.COLUMN_FULL_NAME, Employee.COLUMN_AGE},
                                    null, null, null);
                        default:
                            throw new IllegalArgumentException();

                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    switch (loader.getId()) {
                        case LOADER_EMPLOYEE:
                            mEmployeeAdapter.setEmployers(data);
                            break;
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    switch (loader.getId()) {
                        case LOADER_EMPLOYEE:
                            mEmployeeAdapter.setEmployers(null);
                            break;
                    }
                }
            };
}
