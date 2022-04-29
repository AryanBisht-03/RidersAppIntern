package com.example.ridersappinternship;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RideModel {
    Integer id,distance;
    Integer origin_station_code;
    ArrayList<Integer> station_path;
    Integer destination_station_code;
    String date,map_url,state,city,unformatDate;
    HashMap<String,String> map = new HashMap<String,String>();

    public String getUnformatDate() {
        return unformatDate;
    }

    public void setUnformatDate(String unformatDate) {
        this.unformatDate = unformatDate;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    RideModel(JSONObject object,MainUser user)
    {
        map.put("01","Jan");
        map.put("02","Feb");
        map.put("03","Mar");
        map.put("04","Apr");
        map.put("05","May");
        map.put("06","June");
        map.put("07","July");
        map.put("08","Aug");
        map.put("09","Sep");
        map.put("10","Oct");
        map.put("11","Nov");
        map.put("12","Dec");
        station_path = new ArrayList<Integer>();
        try {
            id = object.getInt("id");
            origin_station_code = object.getInt("origin_station_code");
            JSONArray array = object.getJSONArray("station_path");
            for(int i=0;i<array.length();i++)
                station_path.add(array.getInt(i));

            destination_station_code = object.getInt("destination_station_code");
            unformatDate = object.getString("date");
            String month = map.get(unformatDate.substring(0,2));
            String exactDate = unformatDate.substring(3,5);
            String year = unformatDate.substring(6,10);
            String time = unformatDate.substring(12);
            date = exactDate+"th "+ month+" "+ year+" " +time;
            map_url = object.getString("map_url");
            state = object.getString("state");
            city = object.getString("city");

            Integer closeVal=1000000;
            for(int i=0;i<station_path.size();i++)
            {
                int tmp = Math.abs(station_path.get(i)-user.station_code);
                if(tmp<closeVal)
                {
                    closeVal=tmp;
                }
            }
            distance = closeVal;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrigin_station_code() {
        return origin_station_code;
    }

    public void setOrigin_station_code(Integer origin_station_code) {
        this.origin_station_code = origin_station_code;
    }

    public ArrayList<Integer> getStation_path() {
        return station_path;
    }

    public void setStation_path(ArrayList<Integer> station_path) {
        this.station_path = station_path;
    }

    public Integer getDestination_station_code() {
        return destination_station_code;
    }

    public void setDestination_station_code(Integer destination_station_code) {
        this.destination_station_code = destination_station_code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMap_url() {
        return map_url;
    }

    public void setMap_url(String map_url) {
        this.map_url = map_url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
