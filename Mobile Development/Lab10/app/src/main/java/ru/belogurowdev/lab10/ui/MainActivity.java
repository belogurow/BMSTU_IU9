package ru.belogurowdev.lab10.ui;

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

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private EmployeeDao mEmployeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();



//        employeeDao.insert(new Employee(2, "Max", 20));
//        employeeDao.insert(new Employee(3, "Alex", 43));
//        employeeDao.insert(new Employee(4, "Tom", 34));
//        employeeDao.insert(new Employee(5, "Jane", 29));
//        employeeDao.insert(new Employee(6, "Phillip", 19));

    }

    private void initViews() {
        mEmployeeDao = EmployeeDatabase.getInstance(getApplicationContext()).getEmployeeDao();

        mEmployeeAdapter = new EmployeeAdapter(this, mEmployeeDao.getAllEmployers());

        mRecyclerView = findViewById(R.id.recycler_employers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mEmployeeAdapter);
    }
}
