package com.example.foodie.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodie.R
import com.example.foodie.adapter.Home_adapter
import com.example.foodie.model.Restaurant
import com.example.foodie.util.ConnectionManager
import org.json.JSONException

class Home : Fragment() {
    lateinit var layoutManager: LayoutManager
    lateinit var RecyclerAdapter: Home_adapter
    lateinit var rec_home: RecyclerView
    lateinit var Progresslayout : RelativeLayout

    val RestaurantList = arrayListOf <Restaurant>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val queue = Volley.newRequestQueue(activity as Context)

        layoutManager = LinearLayoutManager(activity)
        rec_home = view.findViewById(R.id.recycler_home)
        Progresslayout = view.findViewById(R.id.ProgressLayout)
        Progresslayout.visibility = View.INVISIBLE

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if(ConnectionManager().checkConnectivity(activity as Context)){
            Progresslayout.visibility = View.VISIBLE
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                try{
                    val response = it.getJSONObject("data")
                    val success = response.getBoolean("success")
                    if(success){
                        val data = response.getJSONArray("data")
                        for(i in 0 until data.length()  ){
                            val ResOject = data.getJSONObject(i)
                            val  res_obj = Restaurant(
                                ResOject.getString("id"),
                                ResOject.getString("name"),
                                ResOject.getString("rating"),
                                ResOject.getString("cost_for_one"),
                                ResOject.getString("image_url"),
                            )
                            RestaurantList.add(res_obj)
                        }
                        RecyclerAdapter = Home_adapter(activity as Context,RestaurantList)
                        rec_home.layoutManager = layoutManager
                        rec_home.adapter = RecyclerAdapter
                        Progresslayout.visibility = View.INVISIBLE
                    }else{
                        Progresslayout.visibility = View.INVISIBLE
                        Toast.makeText(activity as Context,"some error occurred",Toast.LENGTH_SHORT).show()
                    }

                }catch (e: JSONException){
                    Progresslayout.visibility = View.INVISIBLE
                    Toast.makeText(activity as Context,"Something unexpected happened!!",Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                Progresslayout.visibility = View.INVISIBLE
                Toast.makeText(activity as Context ,"some error occurred",Toast.LENGTH_SHORT).show()

            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val header = HashMap<String, String>()
                    header["content"] = "application/json"
                    header["token"] = "2cd8da1c3b65d3"
                    return header
                }

            }
            queue.add(jsonObjectRequest)

        }else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("INTERNET connection not found !")
            dialog.setPositiveButton("Open Settings"){text,listener->
                val settingIntent  = Intent(Settings.ACTION_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)

            }
            dialog.create()
            dialog.show()
        }

        return view
    }
}