package hr.foi.rampu.emedi.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import hr.foi.rampu.emedi.database.AppDatabase

@Entity(
    tableName="appointment_details",
    foreignKeys = [
        ForeignKey(
            entity = Appointment::class,
            parentColumns = ["id"],
            childColumns = ["appointment_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AppointmentDetails (
    @PrimaryKey(autoGenerate=true) val id: Int,
    val description: String,
    val diagnosis: String,
    @ColumnInfo(name="next_steps") val nextSteps: String,
    val medications: String,
    @ColumnInfo(name="appointment_id", index=true) val appointmentId: Int
) {
    @delegate:Ignore
    val appointment: Appointment by lazy {
        AppDatabase.getInstance().getAppointmentsDao().getAppointmentById(appointmentId)
    }
}