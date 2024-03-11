package hr.foi.rampu.emedi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.foi.rampu.emedi.entities.Appointment
import hr.foi.rampu.emedi.entities.Doctor

@Dao
interface AppointmentsDAO {
    @Query("SELECT * FROM appointment WHERE booking_reason_id = :bookingId")
    fun getAppointmentForBooking(bookingId: Int): List<Appointment>

    @Query("SELECT * FROM appointment")
    fun getAllAppointments(): List<Appointment>

    @Query("SELECT * FROM appointment WHERE user_id = :userId ORDER BY appointment_date DESC, appointment_start_time DESC, appointment_end_time DESC")
    fun getAppointmentsForUser(userId: Int): List<Appointment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppointment(vararg appointment: Appointment): List<Long>

    @Query("SELECT * FROM appointment WHERE doctor_id = :doctorId ORDER BY appointment_start_time, appointment_end_time")
    fun getAppointmentsForDoctor(doctorId: Int): List<Appointment>

    @Query("SELECT * FROM appointment WHERE appointment_start_time <= :notificationTimeMillis AND appointment_start_time > :currentTimeMillis")
    fun getAppointmentsForNotification(notificationTimeMillis: Long, currentTimeMillis: Long): List<Appointment>

    @Query("SELECT * FROM appointment WHERE id = :apptId")
    fun getAppointmentById(apptId: Int) : Appointment
}