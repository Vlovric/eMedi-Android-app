package hr.foi.rampu.emedi.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.entities.Doctor
import java.math.RoundingMode

@Entity(
    tableName="reviews",
    foreignKeys = [
        ForeignKey(
            entity = Doctor::class,
            parentColumns = ["id"],
            childColumns = ["doctor_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class Review(
    @PrimaryKey(autoGenerate=true) val id: Int,
    val grade: Int,
    val description: String,
    @ColumnInfo(name="doctor_id", index=true) val doctorId: Int
    //var doctor: Doctor? = null
) {
    @delegate:Ignore
    val doctor: Doctor by lazy {
        AppDatabase.getInstance().getDoctorsDao().getDoctor(doctorId)
    }

    companion object {
        //private val reviews: MutableList<Review> = mutableListOf()

        fun getNewReviewId(): Int {
            val reviewsDAO = AppDatabase.getInstance().getReviewsDao()
            val allReviews = reviewsDAO.getAllReviews()
            var newId = allReviews.count()

            for (review in allReviews) {
                if (review.id > newId) {
                    newId = review.id
                }
            }

            return ++newId
        }

        fun addReview(review: Review) {
            AppDatabase.getInstance().getReviewsDao().insertReview(review)
        }

        fun getReviewsForDoctor(doctor: Doctor): List<Review> {
            return AppDatabase.getInstance().getReviewsDao().getReviewsForDoctor(doctor.id)
        }

        fun getAverageRatingForDoctor(doctor: Doctor): Float {
            var sum = 0f
            val avg: Float
            val reviewsList = AppDatabase.getInstance().getReviewsDao().getReviewsForDoctor(doctor.id)
            reviewsList.forEach {
                sum += it.grade
            }

            return if(reviewsList.isEmpty()){
                0f
            }else{
                sum/reviewsList.size
            }
        }
    }
}
