package ru.belogurowdev.lab10.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import ru.belogurowdev.lab10.model.Employee;

/**
 * Created by alexbelogurow on 29.11.2017.
 */

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM Employee")
    Cursor getAllEmployers();

    @Query("SELECT * FROM Employee WHERE Employee._id = :id")
    Cursor getById(long id);

    @Query("SELECT COUNT(*) FROM Employee")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Employee employers);

    @Update
    int update(Employee... employers);

    @Delete
    int delete(Employee... employers);

    @Query("DELETE FROM Employee WHERE Employee._id = :id")
    int deleteById(long id);

    @Query("DELETE FROM Employee")
    int deleteAll();
}
