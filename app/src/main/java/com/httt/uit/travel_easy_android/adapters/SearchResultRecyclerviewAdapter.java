package com.httt.uit.travel_easy_android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.httt.uit.travel_easy_android.MainActivity;
import com.httt.uit.travel_easy_android.R;
import com.httt.uit.travel_easy_android.model.FlightResults;
import com.httt.uit.travel_easy_android.model.Flights;
import com.httt.uit.travel_easy_android.model.Inbound;
import com.httt.uit.travel_easy_android.model.Itineraries;
import com.httt.uit.travel_easy_android.model.Outbound;
import com.httt.uit.travel_easy_android.model.Results;
import com.httt.uit.travel_easy_android.utils.DateUtils;
import com.httt.uit.travel_easy_android.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TuanAnh on 11/13/17.
 */

public class SearchResultRecyclerviewAdapter extends RecyclerView.Adapter<SearchResultRecyclerviewAdapter.ViewHolder> {
    public static final String AIRLINE_LOGO_URL = "http://pics.avs.io/200/200/";
    private ArrayList<Itineraries> items;
    private Context mContext;
    private int type;
    private String mCurrency;

    Flights obFirstFlight;
    Flights obSecondFlight;
    Date departDate;
    Date arriveDate;
    String obFlight;
    String obDate;
    String obDepartTime;
    String obArriveTime;
    String obDuration;

    Flights ibFirstFlight;
    Flights ibSecondFlight;
    Date ibdepartDate;
    Date ibarriveDate;
    String ibFlight;
    String ibDate;
    String ibDepartTime;
    String ibArriveTime;
    String ibDuration;

    boolean isRoundTrip = true;
    boolean hasStopFlight = true;
    boolean ibhasStopFlight = true;

    public SearchResultRecyclerviewAdapter(Context context, ArrayList<Itineraries> items, int type, String currency) {
        mContext = context;
        this.items = items;
        this.type = type;
        mCurrency = currency;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.grpContainer.setVisibility(View.INVISIBLE);
            return;
        }
        Itineraries itinerary = items.get(position);
        if (itinerary == null)
            return;


        if (type == MainActivity.DEFAULT_ROUND_TRIP)
            isRoundTrip = true;

        if (type == MainActivity.DEFAULT_ONE_WAY)
            isRoundTrip = false;

        obFirstFlight = null;
        obSecondFlight = null;
        departDate = null;
        arriveDate = null;
        obFlight = "";
        obDate = "";
        obDepartTime = "";
        obArriveTime = "";
        obDuration = "";

        double priceDb = Double.parseDouble(itinerary.fare.getTotal_price());

        //Outbound handle
        Outbound outbound = itinerary.getOutbound();

        ArrayList<Flights> outboundFlights = outbound.getFlights();
        if (outboundFlights.size() == 1)
            obFirstFlight = outboundFlights.get(0);
        if (outboundFlights.size() == 2) {
            obFirstFlight = outboundFlights.get(0);
            obSecondFlight = outboundFlights.get(1);
        }

        if (obSecondFlight == null) {
            hasStopFlight = false;
            departDate = DateUtils.parseDateTime(obFirstFlight.getDeparts_at());
            arriveDate = DateUtils.parseDateTime(obFirstFlight.getArrives_at());
            obFlight = obFirstFlight.getMarketing_airline() + obFirstFlight.getFlight_number();
        }
        if (obSecondFlight != null) {
            hasStopFlight = true;
            departDate = DateUtils.parseDateTime(obFirstFlight.getDeparts_at());
            arriveDate = DateUtils.parseDateTime(obSecondFlight.getArrives_at());
            obFlight = obSecondFlight.getOrigin().getAirport();
        }

        obDate = departDate.getDate() + "/" + (departDate.getMonth() + 1);
        obDepartTime = String.format("%02d:%02d", departDate.getHours(), departDate.getMinutes());
        obArriveTime = String.format("%02d:%02d", arriveDate.getHours(), arriveDate.getMinutes());
        obDuration = DateUtils.durationBetween2DateTime(departDate, arriveDate);
        if (!isRoundTrip) {
            holder.mGrpInbound.setVisibility(View.GONE);
        }
        if (isRoundTrip) {
            holder.mGrpInbound.setVisibility(View.VISIBLE);
            //handle inbound flights
            Inbound inbound = itinerary.getInbound();

            ArrayList<Flights> inboundFlights = inbound.getFlights();
            if (inboundFlights.size() == 1)
                ibFirstFlight = inboundFlights.get(0);
            if (inboundFlights.size() == 2) {
                ibFirstFlight = inboundFlights.get(0);
                ibSecondFlight = inboundFlights.get(1);
            }

            if (ibSecondFlight == null) {
                ibhasStopFlight = false;
                ibdepartDate = DateUtils.parseDateTime(ibFirstFlight.getDeparts_at());
                ibarriveDate = DateUtils.parseDateTime(ibFirstFlight.getArrives_at());
                ibFlight = ibFirstFlight.getMarketing_airline() + ibFirstFlight.getFlight_number();
            }
            if (ibSecondFlight != null) {
                ibhasStopFlight = true;
                ibdepartDate = DateUtils.parseDateTime(ibFirstFlight.getDeparts_at());
                ibarriveDate = DateUtils.parseDateTime(ibSecondFlight.getArrives_at());
                ibFlight = ibSecondFlight.getOrigin().getAirport();
            }

            ibDate = ibdepartDate.getDate() + "/" + (ibdepartDate.getMonth() + 1);
            ibDepartTime = String.format("%02d:%02d", ibdepartDate.getHours(), ibdepartDate.getMinutes());
            ibArriveTime = String.format("%02d:%02d", ibarriveDate.getHours(), ibarriveDate.getMinutes());
            ibDuration = DateUtils.durationBetween2DateTime(ibdepartDate, ibarriveDate);
        }

        //fill UI

        holder.tvOutboundDate.setText(obDate);
        holder.tvOutboundDepartTime.setText(obDepartTime);
        if (obFirstFlight != null)
            holder.tvOutboundDepartCode.setText(obFirstFlight.getOrigin().getAirport());
        holder.tvOutboundArriveTime.setText(obArriveTime);
        if (obFirstFlight != null)
            holder.tvOutboundArriveCode.setText(obFirstFlight.getDestination().getAirport());
        if (hasStopFlight) {
            if (obSecondFlight != null)
                holder.tvOutboundArriveCode.setText(obSecondFlight.getDestination().getAirport());
        }
        holder.tvOutboundFLight.setText(obFlight);
        holder.tvOutboundDuration.setText(obDuration);
        if (hasStopFlight) {
            priceDb = priceDb / 4;
            holder.imgOutboundIndicator.setImageResource(R.mipmap.ic_dot_hasstop);
        }
        if (!hasStopFlight) {
            priceDb = priceDb / 2;
            holder.imgOutboundIndicator.setImageResource(R.mipmap.ic_dot_nonstop);
        }


        holder.tvInboundDate.setText(ibDate);
        holder.tvInboundDepartTime.setText(ibDepartTime);
        if (ibFirstFlight != null)
            holder.tvInboundDepartCode.setText(ibFirstFlight.getOrigin().getAirport());
        holder.tvInboundArriveTime.setText(ibArriveTime);
        if (ibFirstFlight != null)
            holder.tvInboundArriveCode.setText(ibFirstFlight.getDestination().getAirport());
        if (ibhasStopFlight) {
            if (ibSecondFlight != null)
                holder.tvInboundArriveCode.setText(ibSecondFlight.getDestination().getAirport());
        }
        holder.tvInboundFLight.setText(ibFlight);
        holder.tvInboundDuration.setText(ibDuration);
        if (ibhasStopFlight)
            holder.imgInboundIndicator.setImageResource(R.mipmap.ic_dot_hasstop);
        if (!ibhasStopFlight)
            holder.imgInboundIndicator.setImageResource(R.mipmap.ic_dot_nonstop);
        String logoUrl = AIRLINE_LOGO_URL + obFirstFlight.getMarketing_airline() + ".png";
        Glide.with(mContext).load(logoUrl).into(holder.imgAirline);
        String price = StringUtils.formatPrice(priceDb, mCurrency);
        holder.tvPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPrice;
        public ImageView imgAirline;


        //inbound
        public TextView tvInboundDate;
        public TextView tvInboundDepartTime;
        public TextView tvInboundDepartCode;
        public TextView tvInboundArriveTime;
        public TextView tvInboundArriveCode;
        public TextView tvInboundDuration;
        public TextView tvInboundFLight;
        public ImageView imgInboundIndicator;
        public LinearLayout mGrpInbound;

        //outbound
        public TextView tvOutboundDate;
        public TextView tvOutboundDepartTime;
        public TextView tvOutboundDepartCode;
        public TextView tvOutboundArriveTime;
        public TextView tvOutboundArriveCode;
        public TextView tvOutboundDuration;
        public TextView tvOutboundFLight;
        public ImageView imgOutboundIndicator;
        public LinearLayout mGrpOutbound;
        public RelativeLayout grpContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            grpContainer = itemView.findViewById(R.id.grpContainer);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            imgAirline = (ImageView) itemView.findViewById(R.id.img_airline);

            mGrpInbound = itemView.findViewById(R.id.grpInBound);
            mGrpOutbound = itemView.findViewById(R.id.grpInBound);
            //inbound
            tvInboundDate = itemView.findViewById(R.id.tv_inbound_date);
            tvInboundDepartTime = itemView.findViewById(R.id.tv_inbound_depart_time);
            tvInboundDepartCode = itemView.findViewById(R.id.tv_inbound_depart_code);
            tvInboundArriveTime = itemView.findViewById(R.id.tv_inbound_arrive_time);
            tvInboundArriveCode = itemView.findViewById(R.id.tv_inbound_arrive_code);
            tvInboundDuration = itemView.findViewById(R.id.tv_inbound_duration);
            tvInboundFLight = itemView.findViewById(R.id.tv_inbound_flight);
            imgInboundIndicator = itemView.findViewById(R.id.img_inbound_indicator);

            //outbound
            tvOutboundDate = itemView.findViewById(R.id.tv_outbound_date);
            tvOutboundDepartTime = itemView.findViewById(R.id.tv_outbound_depart_time);
            tvOutboundDepartCode = itemView.findViewById(R.id.tv_outbound_depart_code);
            tvOutboundArriveTime = itemView.findViewById(R.id.tv_outbound_arrive_time);
            tvOutboundArriveCode = itemView.findViewById(R.id.tv_outbound_arrive_code);
            tvOutboundDuration = itemView.findViewById(R.id.tv_outbound_duration);
            tvOutboundFLight = itemView.findViewById(R.id.tv_outbound_flight);
            imgOutboundIndicator = itemView.findViewById(R.id.img_outbound_indicator);
        }
    }
}
