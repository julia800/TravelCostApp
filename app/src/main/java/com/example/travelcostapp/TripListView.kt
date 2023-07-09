import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelcostapp.R
import com.example.travelcostapp.module.Trip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TripListView : RecyclerView.Adapter<TripListView.TripViewHolder>() {

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val image: ImageView = itemView.findViewById(R.id.imageView)
        private val imageUriBase: Uri = Uri.parse("android.resource://com.example.travelcostapp/drawable/")

        fun bind(trip: Trip) {
            nameTextView.text = trip.name
            val imageUri = if(newTrips.isNotEmpty()) Uri.withAppendedPath(imageUriBase, trip.destination) else Uri.withAppendedPath(Uri.parse(""), "")
            image.setImageURI(imageUri)
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
        val trip = if (newTrips.isNotEmpty()) newTrips[position] else getDummyTrips()[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int {
        return if (newTrips.isNotEmpty()) newTrips.size else getDummyTrips().size
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
                        val startDate = tripData["startDate"] as? String
                        val endDate = tripData["endDate"] as? String
                        val destination = tripData["destination"] as? String

                        if (name != null && startDate != null && endDate != null && destination != null) {
                            val trip = Trip(name, destination, startDate, endDate)
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

    private fun getDummyTrips(): List<Trip> {
        val trip1 = Trip("", "", "", "")
        return listOf(trip1)
    }

    init {
        usersRef.addValueEventListener(valueEventListener)
    }
}
