package com.example.travelcostapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.travelcostapp.module.Trip
import java.util.*

class ImageListAdapter(context: Context, private val tripList: List<Trip>) :
    ArrayAdapter<Trip>(context, R.layout.image_list_item, tripList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.image_list_item, parent, false)

        val trip = tripList[position]

        val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)

        nameTextView.text = trip.name

        val destination =  trip.destination.lowercase(Locale.ROOT)
        when {
            destination == "rom" -> imageView.setImageResource(R.drawable.rom)
            destination == "paris" -> imageView.setImageResource(R.drawable.paris)
            else -> imageView.setImageResource(R.drawable.default_image)
        }

        return itemView
    }
}
