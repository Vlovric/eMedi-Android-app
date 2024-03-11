package hr.foi.rampu.emedi.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import hr.foi.rampu.emedi.database.AppDatabase

@Entity(
    tableName="bookingReasons",
    foreignKeys = [
        ForeignKey(
            entity = Doctor::class,
            parentColumns = ["id"],
            childColumns = ["doctor_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class BookingReason(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val symptoms : String,
    val duration : String,
    val history : String,
    val urgency : String,
    val additional : String,
    @ColumnInfo(name="doctor_id", index=true) val doctorId : Int,
    @ColumnInfo(name="user_id", index=true) val userId : Int
){
    @delegate:Ignore
    val doctor : Doctor by lazy{
        AppDatabase.getInstance().getDoctorsDao().getDoctor(doctorId)
    }
    val user : User by lazy{
        AppDatabase.getInstance().getUsersDao().getUser(userId)
    }
    override fun toString(): String {
        return "BookingReason(id=$id, symptoms=$symptoms, duration=$duration, history=$history, urgency=$urgency, additional=$additional, doctorId=$doctorId, userId=$userId)"
    }
}
