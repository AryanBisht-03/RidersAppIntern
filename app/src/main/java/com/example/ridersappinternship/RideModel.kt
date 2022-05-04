package com.example.ridersappinternship

import org.json.JSONObject
import com.example.ridersappinternship.MainUser
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import java.util.HashMap

class RideModel internal constructor(`object`: JSONObject, user: MainUser) {
    var id: Int? = null
    var distance: Int? = null
    var origin_station_code: Int? = null
    var station_path: ArrayList<Int>
    var destination_station_code: Int? = null
    var date: String? = null
    var map_url: String? = null
    var state: String? = null
    var city: String? = null
    var unformatDate: String? = null
    var map = HashMap<String, String>()

    init {
        map["01"] = "Jan"
        map["02"] = "Feb"
        map["03"] = "Mar"
        map["04"] = "Apr"
        map["05"] = "May"
        map["06"] = "June"
        map["07"] = "July"
        map["08"] = "Aug"
        map["09"] = "Sep"
        map["10"] = "Oct"
        map["11"] = "Nov"
        map["12"] = "Dec"
        station_path = ArrayList()
        try {
            id = `object`.getInt("id")
            origin_station_code = `object`.getInt("origin_station_code")
            val array = `object`.getJSONArray("station_path")
            for (i in 0 until array.length()) station_path.add(array.getInt(i))
            destination_station_code = `object`.getInt("destination_station_code")
            unformatDate = `object`.getString("date")
            val month = map[unformatDate?.substring(0, 2)]
            val exactDate = unformatDate?.substring(3, 5)
            val year = unformatDate?.substring(6, 10)
            val time = unformatDate?.substring(12)
            date = exactDate + "th " + month + " " + year + " " + time
            map_url = `object`.getString("map_url")
            state = `object`.getString("state")
            city = `object`.getString("city")
            var closeVal = 1000000
            for (i in station_path.indices) {
                val tmp = Math.abs(station_path[i] - user.station_code)
                if (tmp < closeVal) {
                    closeVal = tmp
                }
            }
            distance = closeVal
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}