package com.example.ridersappinternship

import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.example.ridersappinternship.RideModel
import com.example.ridersappinternship.recycleAdapter
import com.example.ridersappinternship.MainUser
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.ridersappinternship.R
import android.widget.AdapterView
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout
import com.example.ridersappinternship.HttpsTrustManager
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONException
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.example.ridersappinternship.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

// Spinner state and city and Upcoming and past recycler to be correct.
class MainActivity : AppCompatActivity() {
    private var mRequestQueue: RequestQueue? = null
    private var rides: ArrayList<RideModel>? = null
    private var upcoming: ArrayList<RideModel>? = null
    private var past: ArrayList<RideModel>? = null
    private var fliterList: ArrayList<RideModel>? = null
    var binding: ActivityMainBinding? = null
    var adapter: recycleAdapter? = null
    var user: MainUser? = null
    var state: ArrayList<String>? = null
    var city: ArrayList<String>? = null
    var selectedState: String? = null
    var selectCity: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        supportActionBar!!.hide()
        rides = ArrayList()
        upcoming = ArrayList()
        past = ArrayList()
        fliterList = ArrayList()
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("Nearest"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("Upcoming"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("Past"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("Filters"))
        state = ArrayList()
        val map = HashMap<String, MutableList<String>>()
        city = ArrayList()
        city!!.add("City")
        city!!.add("Dehradun")
        city!!.add("Chamoli")
        map["Uttarakhand"] = city!!
        city = ArrayList()
        city!!.add("City")
        city!!.add("New Delhi")
        city!!.add("Chatra")
        map["Jharkhand"] = city!!
        city = ArrayList()
        city!!.add("City")
        city!!.add("Bhatinda")
        city!!.add("Ambala")
        map["Punjab"] = city!!
        state!!.add("State")
        state!!.add("Uttarakhand")
        state!!.add("Jharkhand")
        state!!.add("Punjab")
        city = ArrayList()
        city!!.add("City")
        val cityAdapterTmp = ArrayAdapter(this@MainActivity, R.layout.spinner_item, city!!)
        cityAdapterTmp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.citySpinner.adapter = cityAdapterTmp
        cityAdapterTmp.setDropDownViewResource(R.layout.spinner_item)
        val stateAdapter = ArrayAdapter(this, R.layout.spinner_item, state!!)
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.stateSpinner.adapter = stateAdapter
        stateAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding!!.stateSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    val stateSelected = adapterView.getItemAtPosition(i).toString()
                    if (stateSelected != "State") {
                        val cityAdapter = ArrayAdapter(this@MainActivity, R.layout.spinner_item, map[stateSelected]!!)
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding!!.citySpinner.adapter = cityAdapter
                        cityAdapter.setDropDownViewResource(R.layout.spinner_item)
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        binding!!.button.setOnClickListener {
            selectedState = binding!!.stateSpinner.selectedItem.toString()
            selectCity = binding!!.citySpinner.selectedItem.toString()
            fliterList!!.clear()
            if (selectedState != "State") {
                for (i in rides!!.indices) {
                    if (selectedState == rides!![i].state) {
                        if (selectCity == "City") fliterList!!.add(rides!![i]) else {
                            if (selectCity == rides!![i].city) fliterList!!.add(rides!![i])
                        }
                    }
                }
                binding!!.cardView.visibility = View.GONE
                adapter = recycleAdapter(this@MainActivity, fliterList!!)
                adapter!!.notifyDataSetChanged()
                binding!!.recyclerView.adapter = adapter
            }
        }
        binding!!.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    binding!!.cardView.visibility = View.GONE

                    adapter = recycleAdapter(this@MainActivity, rides!!)
                    adapter!!.notifyDataSetChanged()
                    binding!!.recyclerView.adapter = adapter
                } else if (tab.position == 1) {
                    binding!!.cardView.visibility = View.GONE

                    adapter = recycleAdapter(this@MainActivity, upcoming!!)
                    adapter!!.notifyDataSetChanged()
                    binding!!.recyclerView.adapter = adapter
                } else if (tab.position == 2) {
                    binding!!.cardView.visibility = View.GONE
//                    Log.d("Aryan", "Selected 2 " + past!!.size)
                    adapter = recycleAdapter(this@MainActivity, past!!)
                    adapter!!.notifyDataSetChanged()
                    binding!!.recyclerView.adapter = adapter
                } else {
                    binding!!.cardView.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        adapter = recycleAdapter(this, rides!!)
        HttpsTrustManager.allowAllSSL()
        val userUrl = "https://assessment.api.vweb.app/user"
        mRequestQueue = Volley.newRequestQueue(this)
        val userReg = JsonObjectRequest(
            Request.Method.GET, userUrl, null,
            { response ->
                try {
                    user = MainUser(
                        response.getInt("station_code"), response.getString("name"),
                        response.getString("url")
                    )
                    binding!!.userName.text = user!!.name
                    Log.d("Aryan", user!!.name + " " + user!!.station_code)
                } catch (e: JSONException) {
                    Log.d("Aryan", "Some exception occurs")
                    e.printStackTrace()
                }
            }) { error -> Log.d("Aryan", "Error is : - " + error.message) }


        val myUrl = "https://assessment.api.vweb.app/rides"
        mRequestQueue = Volley.newRequestQueue(this)
        val req = JsonArrayRequest(
            Request.Method.GET, myUrl, null,
            { response ->
                try {
                    for (i in 0 until response.length()) {
                        val `object` = response.getJSONObject(i)
                        if (user != null) {
                            val data = RideModel(`object`, user!!)
                            rides!!.add(data)
                        }
                    }
                    Log.d("Aryan", "Size is : - " + rides!!.size)
                    for (i in rides!!.indices) {
                        val date = Date()
                        val formatter = SimpleDateFormat("MM/dd/yyyy")
                        val rideDate = rides!![i].unformatDate
                        val str = formatter.format(date)
                        val cond1 = str.substring(0, 2).compareTo(rideDate!!.substring(0, 2))
                        val cond2 = str.substring(3, 5).compareTo(rideDate!!.substring(3, 5))
                        if (cond1 > 0 || cond1 == 0 && cond2 > 0)
                            past!!.add(rides!![i])
                        else
                            upcoming!!.add(rides!![i])
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        rides!!.sortWith(Comparator { r1: RideModel, r2: RideModel ->
                            r1.distance!!.compareTo(r2.distance!!)
                        })
                    }
                    binding!!.tabLayout.getTabAt(1)?.text = "UPCOMING ("+upcoming?.size+")"
                    binding!!.tabLayout.getTabAt(2)?.text = "PAST ("+past?.size+")"
                    adapter!!.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Log.d("Aryan", "Some exception occurs")
                    e.printStackTrace()
                }
            }) {
            Toast.makeText(
                this@MainActivity,
                "There is some in internet please try again later",
                Toast.LENGTH_SHORT
            ).show()
            //                Log.d("Aryan", error.getMessage());
        }
        mRequestQueue!!.add(userReg)
        binding!!.recyclerView.adapter = adapter
        mRequestQueue!!.add(req)
    }
}