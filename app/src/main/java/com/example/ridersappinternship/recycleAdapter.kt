package com.example.ridersappinternship

import android.content.Context
import com.example.ridersappinternship.RideModel
import androidx.recyclerview.widget.RecyclerView
import com.example.ridersappinternship.recycleAdapter.rideViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.ridersappinternship.R
import com.example.ridersappinternship.databinding.RecyclerRidesViewBinding
import java.util.ArrayList

class recycleAdapter(var context: Context, var items: ArrayList<RideModel>) :
    RecyclerView.Adapter<rideViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rideViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_rides_view, parent, false)
        return rideViewHolder(view)
    }

    override  fun onBindViewHolder(holder: rideViewHolder, position: Int) {
        holder.binding.cityName.text = items[position].city
        holder.binding.stateName.text = items[position].state
        holder.binding.rideID.text = items[position].id.toString()
        holder.binding.originState.text = items[position].origin_station_code.toString()
        holder.binding.date.text = items[position].date
        var path = "[ "
        for (i in items[position].station_path.indices) {
            path = path + items[position].station_path[i] + " , "
        }
        path = "$path]"
        holder.binding.statePath.text = path
        holder.binding.distance.text = items[position].distance.toString()
        //Make for path text, distance text,
    }

    override fun getItemCount(): Int {
        return items.size
    }

    public class rideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RecyclerRidesViewBinding

        init {
            binding = RecyclerRidesViewBinding.bind(itemView)
        }
    }
}