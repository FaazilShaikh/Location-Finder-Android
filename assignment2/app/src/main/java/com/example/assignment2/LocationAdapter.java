package com.example.assignment2;


import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{
    private ArrayList<Locations> listdata;
    private ArrayList<Locations> listsource;
    private Timer timer;

    // RecyclerView recyclerView;
    public LocationAdapter(ArrayList<Locations> listdata) {
        this.listdata = listdata;
        this.listsource = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //set text of list items
        final Locations location = listdata.get(holder.getAdapterPosition());
        holder.address.setText("Address: " + location.getAddress());
        holder.latitude.setText("Latitude: " +location.getLatitude());
        holder.longitude.setText("Longitude: " +location.getLongitude());

        //on click of the list items, send an intent to the create activity with all the data for that list item
        if (holder !=null ){
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent (view.getContext(), Create.class);

                    Bundle bundle = new Bundle();
                    bundle.putInt("Id", location.getId());
                    bundle.putString("address", location.getAddress());
                    bundle.putString("longitude", location.getLongitude());
                    bundle.putString("latitude",location.getLatitude());

                    intent.putExtras(bundle);

                    view.getContext().startActivity(intent, bundle);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView address;
        public TextView latitude;
        public TextView longitude;


        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            this.address = (TextView) itemView.findViewById(R.id.address);
            this.latitude = (TextView) itemView.findViewById(R.id.latitude);
            this.longitude = (TextView) itemView.findViewById(R.id.longitude);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.layout);
        }
    }
    //search notes on a timer, searching will lag a lot so this is to ensure there arent consecutive searches when typing in letters
    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    listdata = listsource;
                } else {
                    ArrayList<Locations> temp = new ArrayList<>();
                    for (Locations note : listdata) {
                        if (note.getAddress().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                note.getLatitude().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                note.getLongitude().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    listdata = temp;
                }

                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }
}
