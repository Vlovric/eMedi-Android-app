package hr.foi.rampu.emedi.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hr.foi.rampu.emedi.converters.DateConverter
import hr.foi.rampu.emedi.database.AppDatabase
import java.util.Date

@Entity(
    tableName="appointment",
    foreignKeys = [
        ForeignKey(
            entity = Doctor::class,
            parentColumns = ["id"],
            childColumns = ["doctor_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookingReason::class,
            parentColumns = ["id"],
            childColumns = ["booking_reason_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(DateConverter::class)
data class Appointment (
    @PrimaryKey(autoGenerate=true) val id: Int,
    @ColumnInfo(name="appointment_date") val appointmentDate: Date, // Koristiti samo datum
    @ColumnInfo(name="appointment_start_time") val appointmentStartTime: Date, // Koristiti samo vrijeme
    @ColumnInfo(name="appointment_end_time") val appointmentEndTime: Date, // koristiti samo vrijeme
    @ColumnInfo(name="doctor_id", index=true) val doctorId: Int,
    @ColumnInfo(name="user_id", index=true) val userId: Int,
    @ColumnInfo(name="booking_reason_id", index=true) val bookingReasonId: Int
) {
    @delegate:Ignore
    val doctor: Doctor by lazy {
        AppDatabase.getInstance().getDoctorsDao().getDoctor(doctorId)
    }

    @delegate:Ignore
    val user: User by lazy {
        AppDatabase.getInstance().getUsersDao().getUser(userId)
    }

    @delegate:Ignore
    val bookingReason: BookingReason by lazy {
        AppDatabase.getInstance().getBookingReasonsDao().getBookingReason(bookingReasonId)
    }
}