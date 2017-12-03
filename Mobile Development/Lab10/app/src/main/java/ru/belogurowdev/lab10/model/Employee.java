package ru.belogurowdev.lab10.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

/**
 * Created by alexbelogurow on 29.11.2017.
 */

@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    @ColumnInfo(name = COLUMN_FULL_NAME)
    private String fullName;

    @ColumnInfo(name = COLUMN_AGE)
    private Integer age;

    @Ignore
    public static final String COLUMN_ID = BaseColumns._ID;
    @Ignore
    public static final String COLUMN_AGE = "age";
    @Ignore
    public static final String COLUMN_FULL_NAME = "fullName";


    public Employee(String fullName, Integer age) {
        this.fullName = fullName;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("id=").append(id);
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }
}
