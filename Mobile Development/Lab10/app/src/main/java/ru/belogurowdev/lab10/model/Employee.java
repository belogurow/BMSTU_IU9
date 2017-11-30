package ru.belogurowdev.lab10.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
/**
 * Created by alexbelogurow on 29.11.2017.
 */

@Entity
public class Employee {
    @PrimaryKey
    private long id;
    private String fullName;
    private Integer age;


    public Employee(long id, String fullName, Integer age) {
        this.id = id;
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
