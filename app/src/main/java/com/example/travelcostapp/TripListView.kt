import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelcostapp.R
import com.example.travelcostapp.module.Travelers
import com.example.travelcostapp.module.Trip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TripListView : RecyclerView.Adapter<TripListView.TripViewHolder>() {
    private var imageUri: Uri = Uri.withAppendedPath(Uri.parse(""), "")

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val image: ImageView = itemView.findViewById(R.id.imageView)
        private val imageUriBase: Uri =
            Uri.parse("android.resource://com.example.travelcostapp/drawable/")

        fun bind(trip: Trip) {
            nameTextView.text = trip.name
            validateImg(trip.destination, trip)
            image.setImageURI(imageUri)
        }

        private fun validateImg(image: String, trip: Trip) {
            if (newTrips.isNotEmpty())
                if (image == "paris" || image == "rom")
                    imageUri = Uri.withAppendedPath(imageUriBase, trip.destination)
                else imageUri = Uri.withAppendedPath(imageUriBase, "default_image")
            else
                imageUri = Uri.withAppendedPath(Uri.parse(""), "")
        }
    }

    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("trips")
    private var newTrips: MutableList<Trip> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = if (newTrips.isNotEmpty()) newTrips[position] else getEmptyList()[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int {
        return if (newTrips.isNotEmpty()) newTrips.size else getEmptyList().size
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.value
            if (value is Map<*, *>) {
                Log.d("Firebase", "$value")
                newTrips.clear()
                for ((_, tripData) in value) {
                    if (tripData is Map<*, *>) {
                        val name = tripData["name"] as? String
                        val destination = tripData["destination"] as? String
                        val days = (tripData["days"] as? Long)?.toInt()
                        val amountOfTravelers = (tripData["amountOfTravelers"] as? Long)?.toInt()
                        val travelers = tripData["travelers"] as? Travelers

                        if (name != null && destination != null && days != null && amountOfTravelers != null && travelers != null) {
                            val trip = Trip(name, destination, days , amountOfTravelers, listOf(travelers))
                            newTrips.add(trip)
                        }
                    }
                }
                notifyDataSetChanged()
            } else {
                println("Sorry, I couldn't grab your data :(")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("Firebase", "Data retrieval cancelled: ${databaseError.message}")
        }
    }

    private fun getEmptyList(): List<Trip> {
        return listOf()
    }

    init {
        usersRef.addValueEventListener(valueEventListener)
    }
}
