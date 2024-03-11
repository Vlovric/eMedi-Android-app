import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RatingBar
import android.widget.TextView
import hr.foi.rampu.emedi.R
import hr.foi.rampu.emedi.entities.Review
import hr.foi.rampu.emedi.helpers.TextSizeUtility
import org.w3c.dom.Text

class ReviewAdapter(private val context: Context, private val reviews: List<Review>) : BaseAdapter() {


    override fun getCount(): Int {
        return reviews.size

    }

    override fun getItem(position: Int): Any {
        return reviews[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.single_review, parent, false)

        val ratingBar = rowView.findViewById<RatingBar>(R.id.ratingBar)
        val textViewDescription = rowView.findViewById<TextView>(R.id.textViewDescription)

        val review = getItem(position) as Review

        ratingBar.rating = review.grade.toFloat()
        textViewDescription.text = "Description: ${review.description}"

        return rowView
    }
}
