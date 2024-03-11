package hr.foi.rampu.emedi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.foi.rampu.emedi.entities.BookingReason

@Dao
interface BookingReasonsDAO {
    @Query("SELECT * FROM bookingReasons WHERE id = :id")
    fun getBookingReason(id: Int): BookingReason

    @Query("SELECT COUNT(*) FROM bookingReasons")
    fun getBookingReasonCount(): Int

    @Query("DELETE FROM bookingReasons")
    fun deleteAllBookingReasons()

    @Query("SELECT * FROM bookingReasons")
    fun getAllBookingReasons() : List<BookingReason>

    @Query("SELECT * FROM bookingReasons WHERE doctor_id = :doctorId AND user_id = :userId")
    fun getBookingsForDoctorAndUser(doctorId: Int, userId: Int): List<BookingReason>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookingReason(vararg bookingReason :BookingReason): List<Long>
}