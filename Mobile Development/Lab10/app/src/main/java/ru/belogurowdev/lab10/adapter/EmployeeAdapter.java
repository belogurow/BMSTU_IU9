package ru.belogurowdev.lab10.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private Cursor mCursor;

    public EmployeeAdapter(Context context) {
        mContext = context;
    }

    public void setEmployers(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
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
        if (mCursor.moveToPosition(position)) {

            holder.mTextName.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(Employee.COLUMN_FULL_NAME)));

            holder.mTextAge.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(Employee.COLUMN_AGE)));

            Log.d("employee", String.valueOf(mCursor.getLong(mCursor.getColumnIndexOrThrow(Employee.COLUMN_ID))));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }
}
