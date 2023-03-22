package com.example.foodie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.R
import com.example.foodie.activity.Restaurant_Details
import com.example.foodie.model.Restaurant
import com.squareup.picasso.Picasso

class Home_adapter(val context: Context, val itemList : ArrayList<Restaurant>) : RecyclerView.Adapter<Home_adapter.HomeViewHolder>() {
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtText :TextView = view.findViewById(R.id.txtText)
        val txtPrice :TextView = view.findViewById(R.id.txtPrice)
        val txtRating :TextView = view.findViewById(R.id.txtRating)
        val imgFoodImg: ImageView  = view.findViewById(R.id.imgFoodImg)
        val Content:LinearLayout = view.findViewById(R.id.Clickable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_single_row,parent,false)
        return HomeViewHolder(view)

    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val Res  = itemList[position]
        holder.txtPrice.text = Res.Price
        holder.txtText.text = Res.Name
        holder.txtText.tag = Res.id + ""
        holder.txtRating.text = Res.Rating
//        holder.imgFoodImg.setImageResource(Res.Imageview)
        Picasso.get().load(Res.Imageview).error(R.drawable.image_default).into(holder.imgFoodImg)

        holder.Content.setOnClickListener {
            println("The id is ${holder.txtText.tag.toString()}")
            val intent = Intent(context, Restaurant_Details::class.java)
            intent.putExtra("Res_id",holder.txtText.tag.toString())
            intent.putExtra("Res_Name",holder.txtText.text.toString())
            context.startActivity(intent)
        }

    }
}