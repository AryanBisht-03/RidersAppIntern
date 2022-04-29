package com.example.ridersappinternship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ridersappinternship.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

// Spinner state and city and Upcoming and past recycler to be correct.
public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private ArrayList<RideModel> rides,upcoming,past,fliterList;
    ActivityMainBinding binding;
    recycleAdapter adapter;
    MainUser user=null;
    ArrayList<String> state,city;
    String selectedState,selectCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        rides = new ArrayList<RideModel>();
        upcoming = new ArrayList<RideModel>();
        past = new ArrayList<RideModel>();
        fliterList = new ArrayList<RideModel>();
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Nearest"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Past"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Filters"));

        state = new ArrayList<>();
        HashMap<String,ArrayList<String>> map = new HashMap<>();
        city = new ArrayList<>();
        city.add("City");
        city.add("Dehradun");
        city.add("Chamoli");
        map.put("Uttarakhand",city);

        city = new ArrayList<>();
        city.add("City");
        city.add("New Delhi");
        city.add("Chatra");
        map.put("Jharkhand",city);

        city= new ArrayList<>();
        city.add("City");
        city.add("Bhatinda");
        city.add("Ambala");
        map.put("Punjab",city);

        state.add("State");
        state.add("Uttarakhand");
        state.add("Jharkhand");
        state.add("Punjab");

        city = new ArrayList<>();
        city.add("City");
        ArrayAdapter<String> cityAdapterTmp = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_item,city );
        cityAdapterTmp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.citySpinner.setAdapter(cityAdapterTmp);
        cityAdapterTmp.setDropDownViewResource(R.layout.spinner_item);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, state);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.stateSpinner.setAdapter(stateAdapter);
        stateAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateSelected = adapterView.getItemAtPosition(i).toString();
                if(!stateSelected.equals("State"))
                {
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_item, map.get(stateSelected));
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.citySpinner.setAdapter(cityAdapter);
                    cityAdapter.setDropDownViewResource(R.layout.spinner_item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedState = binding.stateSpinner.getSelectedItem().toString();
                selectCity = binding.citySpinner.getSelectedItem().toString();
                fliterList.clear();
                if(!selectedState.equals("State"))
                {
                    for(int i=0;i<rides.size();i++)
                    {
                        if(selectedState.equals(rides.get(i).getState()))
                        {
                            if(selectCity.equals("City"))
                                fliterList.add(rides.get(i));
                            else
                            {
                                if(selectCity.equals(rides.get(i).getCity()))
                                    fliterList.add(rides.get(i));
                            }
                        }
                    }
                    binding.cardView.setVisibility(View.GONE);
                    adapter = new recycleAdapter(MainActivity.this,fliterList);
                    adapter.notifyDataSetChanged();
                    binding.recyclerView.setAdapter(adapter);
                }
            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0)
                {
                    binding.cardView.setVisibility(View.GONE);
                    Log.d("Aryan","Selected 0 "+rides.size());
                    adapter = new recycleAdapter(MainActivity.this,rides);
                    adapter.notifyDataSetChanged();
                    binding.recyclerView.setAdapter(adapter);
                }
                else if(tab.getPosition() == 1) {
                    binding.cardView.setVisibility(View.GONE);
                    Log.d("Aryan", "Selected 1 " + upcoming.size());
                    adapter = new recycleAdapter(MainActivity.this,upcoming);
                    adapter.notifyDataSetChanged();
                    binding.recyclerView.setAdapter(adapter);
                }
                else if(tab.getPosition() == 2){
                    binding.cardView.setVisibility(View.GONE);
                    Log.d("Aryan", "Selected 2 " + past.size());
                    adapter = new recycleAdapter(MainActivity.this,past);
                    adapter.notifyDataSetChanged();
                    binding.recyclerView.setAdapter(adapter);
                }
                else {
                    binding.cardView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adapter = new recycleAdapter(this,rides);
        HttpsTrustManager.allowAllSSL();
        String userUrl = "https://assessment.api.vweb.app/user";
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest userReg = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = new MainUser(response.getInt("station_code"),response.getString("name"),
                                    response.getString("url"));
                            binding.userName.setText(user.name);
                            Log.d("Aryan",user.name+" "+user.station_code);
                        } catch (JSONException e) {
                            Log.d("Aryan","Some exception occurs");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Aryan","Error is : - " + error.getMessage());
            }
        });

//        user = new MainUser(25,"name", "url");
        String myUrl = "https://assessment.api.vweb.app/rides";
        mRequestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, myUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++)
                            {
                                JSONObject object = response.getJSONObject(i);
                                if(user!=null){
                                    RideModel data = new RideModel(object,user);
                                    rides.add(data);
                                }
                            }
                            Log.d("Aryan","Size is : - "+ rides.size());
                            for(int i=0;i<rides.size();i++){
                                Date date = new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                String rideDate = rides.get(i).getUnformatDate();
                                String str = formatter.format(date);
                                Log.d("Aryan",str+" "+rideDate);
                                int cond1 = str.substring(0,2).compareTo(rideDate.substring(0,2));
                                int cond2 = str.substring(3,5).compareTo(rideDate.substring(3,5));
                                if(cond1 > 0 || (cond1 == 0 && cond2>0))
                                    past.add(rides.get(i));
                                else
                                    upcoming.add(rides.get(i));
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                rides.sort((r1,r2)-> r1.distance.compareTo(r2.distance));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.d("Aryan","Some exception occurs");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "There is some in internet please try again later", Toast.LENGTH_SHORT).show();
//                Log.d("Aryan", error.getMessage());
            }
        });
        mRequestQueue.add(userReg);
        binding.recyclerView.setAdapter(adapter);
        mRequestQueue.add(req);
    }
}