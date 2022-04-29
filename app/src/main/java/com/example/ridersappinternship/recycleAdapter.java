package com.example.ridersappinternship;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridersappinternship.databinding.RecyclerRidesViewBinding;

import java.util.ArrayList;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.rideViewHolder> {

    Context context;
    ArrayList<RideModel> items;

    public recycleAdapter(Context context, ArrayList<RideModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public rideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_rides_view, parent, false);
        return new rideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rideViewHolder holder, int position) {
        holder.binding.cityName.setText(items.get(position).getCity());
        holder.binding.stateName.setText(items.get(position).getState());
        holder.binding.rideID.setText(items.get(position).getId().toString());
        holder.binding.originState.setText(items.get(position).getOrigin_station_code().toString());
        holder.binding.date.setText(items.get(position).getDate());
        String path = "[ ";
        for(int i=0;i<items.get(position).station_path.size();i++)
        {
            path = path+ items.get(position).station_path.get(i) +" , ";
        }
        path=path+"]";
        holder.binding.statePath.setText(path);
        holder.binding.distance.setText(items.get(position).distance.toString());
        //Make for path text, distance text,
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class rideViewHolder extends RecyclerView.ViewHolder{

        RecyclerRidesViewBinding binding;
        public rideViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerRidesViewBinding.bind(itemView);
        }
    }
}
