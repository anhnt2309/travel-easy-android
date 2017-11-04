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
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.model.FlightClass;

import java.util.ArrayList;

/**
 * Created by TuanAnh on 11/3/17.
 */

public class ClassSpinnerAdapter extends ArrayAdapter<FlightClass> implements ThemedSpinnerAdapter {
    public Context mContext;
    public ArrayList<FlightClass> mModels;


    public ClassSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<FlightClass> models) {
        super(context, resource);
        mContext = context;
        mModels = models;

    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Nullable
    @Override
    public FlightClass getItem(int position) {
        return mModels.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_flight_class, null, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        handleView(convertView, position, false);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_flight_class, null, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        handleView(convertView, position, true);
        return convertView;
    }


    static class ViewHolder {
        ImageView icon;
        TextView tvNameEng;
        TextView tvNameVN;
        ImageView imgDropdown;

        public ViewHolder(View view) {
            icon = view.findViewById(R.id.img_class_icon);
            tvNameEng = view.findViewById(R.id.tv_class_eng);
            tvNameVN = view.findViewById(R.id.tv_class_vn);
            imgDropdown = view.findViewById(R.id.img_dropdown);
        }
    }

    private void handleView(View convertView, int position, boolean isDropdown) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        FlightClass model = getItem(position);
        if (model == null)
            return;

        viewHolder.tvNameEng.setText(model.getClassEng());
        viewHolder.tvNameVN.setText(model.getClassVn());
        viewHolder.icon.setImageResource(model.getIcon());
        if (isDropdown)
            viewHolder.imgDropdown.setVisibility(View.GONE);
    }
}
