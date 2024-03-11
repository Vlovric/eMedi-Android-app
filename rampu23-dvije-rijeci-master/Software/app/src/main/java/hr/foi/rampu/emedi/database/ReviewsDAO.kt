package hr.foi.rampu.emedi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.foi.rampu.emedi.entities.Review
import hr.foi.rampu.emedi.entities.User

@Dao
interface ReviewsDAO {
    @Query("SELECT * FROM reviews WHERE doctor_id = :doctorId")
    fun getReviewsForDoctor(doctorId: Int) : List<Review>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(vararg review: Review): List<Long>

    @Query("SELECT * FROM reviews")
    fun getAllReviews(): List<Review>
}