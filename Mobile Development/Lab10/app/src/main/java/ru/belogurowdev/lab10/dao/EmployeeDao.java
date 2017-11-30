package ru.belogurowdev.lab10.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.belogurowdev.lab10.model.Employee;

/**
 * Created by alexbelogurow on 29.11.2017.
 */

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM Employee")
    List<Employee> getAllEmployers();

    @Insert
    void insert(Employee... employers);

    @Update
    void update(Employee... employers);

    @Delete
    void delete(Employee... employers);
}
