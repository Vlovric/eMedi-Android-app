package hr.foi.rampu.emedi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.foi.rampu.emedi.entities.AppointmentDetails

@Dao
interface AppointmentDetailsDAO {
    @Query("SELECT * FROM appointment_details")
    fun getAllAppointmentDetails(): List<AppointmentDetails>

    @Query("SELECT * FROM appointment_details WHERE appointment_id = :appointmentId")
    fun getAppointmentDetailsForAppointment(appointmentId: Int): List<AppointmentDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppointmentDetails(vararg appointmentDetails: AppointmentDetails): List<Long>
}