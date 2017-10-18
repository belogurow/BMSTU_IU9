package ru.belogurowdev.lab7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import ru.belogurowdev.lab7.R;
import ru.belogurowdev.lab7.model.Result;

/**
 * Created by alexbelogurow on 18.10.2017.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private Context mContext;
    private List<Result> mPlacesList;

    public PlacesAdapter(Context context) {
        mContext = context;
    }

    public void setPlacesList(List<Result> placesList) {
        mPlacesList = placesList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewPlaceName;
        private TextView mTextViewPlaceAddress;
        private RatingBar mRatingBar;
        private TextView mTextViewRating;


        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewPlaceName = itemView.findViewById(R.id.item_place_text_name);
            mTextViewPlaceAddress = itemView.findViewById(R.id.item_place_text_address);
            mRatingBar = itemView.findViewById(R.id.item_place_rating);
            mTextViewRating = itemView.findViewById(R.id.item_place_rating_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result place = mPlacesList.get(position);

        holder.mTextViewPlaceName.setText(place.getName());
        holder.mTextViewPlaceAddress.setText(place.getFormattedAddress());

        if (place.getRating() != null) {
            holder.mTextViewRating.setText(place.getRating().toString());
            holder.mTextViewRating.setTextColor(mContext.getResources().getColor(R.color.accent_material_light));
            holder.mRatingBar.setRating(place.getRating().floatValue());
        }
    }

    @Override
    public int getItemCount() {
        if (mPlacesList != null) {
            return mPlacesList.size();
        } else {
            return 0;
        }
    }


}
