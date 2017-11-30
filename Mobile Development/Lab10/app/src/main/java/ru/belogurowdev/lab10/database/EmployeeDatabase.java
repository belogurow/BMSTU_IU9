package ru.belogurowdev.lab10.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ru.belogurowdev.lab10.dao.EmployeeDao;
import ru.belogurowdev.lab10.model.Employee;

/**
 * Created by alexbelogurow on 29.11.2017.
 */

@Database(entities = {Employee.class}, version = 1)
public abstract class EmployeeDatabase extends RoomDatabase {

    private static final String DB_NAME = "employeeDatabase.db";
    private static volatile EmployeeDatabase sInstance;

    public static synchronized EmployeeDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = create(context);
        }
        return sInstance;
    }

    private static EmployeeDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                EmployeeDatabase.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract EmployeeDao getEmployeeDao();
}
