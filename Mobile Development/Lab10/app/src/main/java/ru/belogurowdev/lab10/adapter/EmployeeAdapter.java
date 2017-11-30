package ru.belogurowdev.lab10.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.belogurowdev.lab10.R;
import ru.belogurowdev.lab10.model.Employee;

/**
 * Created by alexbelogurow on 29.11.2017.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private Context mContext;
    private List<Employee> mEmployeeList;

    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        mContext = context;
        mEmployeeList = employeeList;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;
        private TextView mTextAge;

        public EmployeeViewHolder(View itemView) {
            super(itemView);

            mTextName = itemView.findViewById(R.id.text_name);
            mTextAge = itemView.findViewById(R.id.text_age);
        }
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.employee_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        Employee employee = mEmployeeList.get(position);

        holder.mTextName.setText(employee.getFullName());
        holder.mTextAge.setText(employee.getAge().toString());
    }

    @Override
    public int getItemCount() {
        return mEmployeeList.size();
    }
}
