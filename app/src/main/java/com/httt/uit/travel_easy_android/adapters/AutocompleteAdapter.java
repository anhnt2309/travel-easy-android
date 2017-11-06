package com.httt.uit.travel_easy_android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.zip.Inflater;

import me.grantland.widget.AutofitTextView;

/**
 * Created by TuanAnh on 11/5/17.
 */

public class AutocompleteAdapter extends ArrayAdapter<AutoCompleteAirport> {
    private ArrayList<AutoCompleteAirport> mModels;
    private Context mContext;
    private boolean isNearBy = false;

    public AutocompleteAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<AutoCompleteAirport> models, boolean isNearBy) {
        super(context, resource);
        mContext = context;
        mModels = models;
        this.isNearBy = isNearBy;
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_airport_search, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        AutoCompleteAirport model = mModels.get(position);
        //reset
        holder.tvCode.setText("");
        holder.tvCityName.setText("");
        holder.tvAirportName.setText("");

        if (model == null)
            return convertView;
        holder.tvCode.setText(model.value);
        holder.tvAirportName.setText(model.airport_name);
        if (isNearBy) {
            holder.grpDistance.setVisibility(View.VISIBLE);
            if (model.distance != 0) {
                String roundedDistance = String.format("%.0f", model.distance);
                String distance = roundedDistance + " km";
                holder.tvDistance.setText(distance);
                holder.tvCode.setText(model.airport);
            }
        } else {
            holder.grpDistance.setVisibility(View.GONE);
        }
        if (model.hasCityName) {
            holder.tvCityName.setVisibility(View.VISIBLE);
            holder.imgCity.setVisibility(View.VISIBLE);

            holder.tvCityName.setText(model.city_name);

        } else {
            holder.tvCityName.setVisibility(View.GONE);
            holder.imgCity.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        AutofitTextView tvAirportName;
        TextView tvCityName;
        ImageView imgCity;
        TextView tvCode;
        ShineButton sbCheck;

        LinearLayout grpDistance;
        TextView tvDistance;

        public ViewHolder(View view) {
            tvAirportName = view.findViewById(R.id.tv_airport_name);
            tvCityName = view.findViewById(R.id.tv_city_name);
            imgCity = view.findViewById(R.id.img_city);
            tvCode = view.findViewById(R.id.tv_code);
            sbCheck = view.findViewById(R.id.shine_button);

            grpDistance = view.findViewById(R.id.grpDistance);
            tvDistance = view.findViewById(R.id.tv_distance);
        }


    }
    public interface OnSbClickListener {
        void onSbClick( int position);
    }
    public OnSbClickListener mOnSbClickListener;
    public void setSbClickListener(OnSbClickListener onSbClickListener){
        mOnSbClickListener = onSbClickListener;
    }
}
