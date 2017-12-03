package ru.belogurowdev.lab10.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.belogurowdev.lab10.dao.EmployeeDao;
import ru.belogurowdev.lab10.database.EmployeeDatabase;
import ru.belogurowdev.lab10.model.Employee;

/**
 * Created by alexbelogurow on 29.11.2017.
 */

public class SampleContentProvider extends ContentProvider {

    private static final String TAG = SampleContentProvider.class.getSimpleName();

    public static final String AUTHORITY = "ru.belogurowdev.lab10.provider";

    public static final String URI_EMPLOYEE = "content://" + AUTHORITY + "/" + "employee";

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int MULTIPLE_ROWS = 1;
    private static final int SINGLE_ROW = 2;

    static {
        MATCHER.addURI(AUTHORITY, "employee", MULTIPLE_ROWS);
        MATCHER.addURI(AUTHORITY, "employee" + "/#", SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);

        if (code == MULTIPLE_ROWS || code == SINGLE_ROW) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            EmployeeDao employeeDao = EmployeeDatabase.getInstance(context).getEmployeeDao();

            final Cursor cursor;
            if (code == MULTIPLE_ROWS) {
                cursor = employeeDao.getAllEmployers();
            } else {
                cursor = employeeDao.getById(ContentUris.parseId(uri));
            }

            cursor.setNotificationUri(context.getContentResolver(), uri);

            Log.d(TAG + " query", uri.toString());
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case MULTIPLE_ROWS:
                final Context context = getContext();
                if (context == null || values == null) {
                    return null;
                }

                Employee employee = new Employee(
                    values.getAsString(Employee.COLUMN_FULL_NAME),
                    values.getAsInteger(Employee.COLUMN_AGE));


                final long employeeId = EmployeeDatabase.getInstance(context).getEmployeeDao()
                        .insert(employee);

                context.getContentResolver().notifyChange(uri, null);

                Log.d(TAG + " insert", uri.toString());
                return ContentUris.withAppendedId(uri, employeeId);
            case SINGLE_ROW:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final Context context = getContext();
        if (context == null) {
            return 0;
        }

        int count;

        switch (MATCHER.match(uri)) {
            case MULTIPLE_ROWS:
                count = EmployeeDatabase.getInstance(context).getEmployeeDao()
                        .deleteAll();
                break;
            case SINGLE_ROW:
                count = EmployeeDatabase.getInstance(context).getEmployeeDao()
                        .deleteById(ContentUris.parseId(uri));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        context.getContentResolver().notifyChange(uri, null);

        Log.d(TAG + " delete", uri.toString() + " - " + count);
        return count;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case MULTIPLE_ROWS:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case SINGLE_ROW:
                final Context context = getContext();
                if (context == null || values == null) {
                    return 0;
                }

                final Employee employee = new Employee(
                    values.getAsString(Employee.COLUMN_FULL_NAME),
                    values.getAsInteger(Employee.COLUMN_AGE)
                );
                employee.setId(ContentUris.parseId(uri));

                final int count = EmployeeDatabase.getInstance(context).getEmployeeDao()
                        .update(employee);

                context.getContentResolver().notifyChange(uri, null);

                Log.d(TAG + " update", uri.toString() + " - " + count);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
