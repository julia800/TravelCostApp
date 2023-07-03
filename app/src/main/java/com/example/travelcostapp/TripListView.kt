import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelcostapp.R
import com.example.travelcostapp.module.Trip

class TripListView() :
    RecyclerView.Adapter<TripListView.TripViewHolder>() {

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val image: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(trip: Trip) {
            nameTextView.text = trip.name

            val imageUri = Uri.parse("android.resource://com.example.travelcostapp/drawable/" + trip.destination)
            image.setImageURI(imageUri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = getDummyTrips()[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int {
        return getDummyTrips().size
    }

    private fun getDummyTrips(): List<Trip> {
        val trip1 = Trip("1", "ROM", "rom", "2023-06-01", "2023-06-05")
        val trip2 = Trip("2", "PARIS", "paris", "2023-07-01", "2023-07-10")
        val trip3 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")
        val trip4 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")
        val trip5 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")
        val trip6 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")
        val trip7 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")
        val trip8 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")
        val trip9 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")
        val trip10 = Trip("3", "Trip 3", "rom", "2023-08-01", "2023-08-15")

        return listOf(trip1, trip2, trip3, trip4, trip5, trip6, trip7, trip8, trip9, trip10)
    }
}
