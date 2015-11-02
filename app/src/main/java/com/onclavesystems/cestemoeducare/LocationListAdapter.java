package com.onclavesystems.cestemoeducare;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by root on 2/11/15.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    private String[] centerNames, addresses;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public LocationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView tv_name = (TextView) cardView.findViewById(R.id.name_id);
        TextView tv_address = (TextView) cardView.findViewById(R.id.address_id);
        tv_name.setText(centerNames[position]);
        tv_address.setText(addresses[position]);
    }

    @Override
    public int getItemCount() {
        return centerNames.length;
    }
}
