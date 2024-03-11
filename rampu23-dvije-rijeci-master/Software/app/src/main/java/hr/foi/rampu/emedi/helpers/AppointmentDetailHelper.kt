package hr.foi.rampu.emedi.helpers

import android.util.Log
import hr.foi.rampu.emedi.database.AppDatabase
import hr.foi.rampu.emedi.database.AppointmentDetailsDAO
import hr.foi.rampu.emedi.database.AppointmentsDAO
import hr.foi.rampu.emedi.entities.AppointmentDetails
import kotlin.random.Random

object AppointmentDetailHelper {
    private val randomDetails: List<AppointmentDetails> = listOf(
        AppointmentDetails (
            id = 1,
            description = "test1",
            diagnosis = "test1",
            nextSteps = "test1",
            medications = "test1",
            appointmentId = 1
        ),
        AppointmentDetails (
            id = 1,
            description = "test2",
            diagnosis = "test2",
            nextSteps = "test2",
            medications = "test2",
            appointmentId = 1
        ),
        AppointmentDetails (
            id = 1,
            description = "test3",
            diagnosis = "test3",
            nextSteps = "test3",
            medications = "test3",
            appointmentId = 1
        ),
        AppointmentDetails (
            id = 1,
            description = "test4",
            diagnosis = "test4",
            nextSteps = "test4",
            medications = "test4",
            appointmentId = 1
        ),
        AppointmentDetails (
            id = 1,
            description = "test5",
            diagnosis = "test5",
            nextSteps = "test5",
            medications = "test5",
            appointmentId = 1
        )
    )

    private fun generateAppointmentDetails(newId: Int, appointmentId: Int): AppointmentDetails {
        val description = randomDetails[Random.nextInt(0, randomDetails.size)].description
        val diagnosis = randomDetails[Random.nextInt(0, randomDetails.size)].diagnosis
        val nextSteps = randomDetails[Random.nextInt(0, randomDetails.size)].nextSteps
        val medications = randomDetails[Random.nextInt(0, randomDetails.size)].medications

        return AppointmentDetails(newId, description, diagnosis, nextSteps, medications, appointmentId)
    }

    fun saveNewAppointmentDetail(appointmentId: Int, dao: AppointmentDetailsDAO): List<AppointmentDetails> {
        val newId = getNewAppointmentDetailId(dao)

        var newAppointmentDetail = generateAppointmentDetails(newId, appointmentId)

        dao.insertAppointmentDetails(newAppointmentDetail)
        return listOf(newAppointmentDetail)
    }

    private fun getNewAppointmentDetailId(appointmentDetailsDAO: AppointmentDetailsDAO): Int {
        val allDetails = appointmentDetailsDAO.getAllAppointmentDetails()
        var newId = allDetails.count()

        for (detail in allDetails) {
            if (detail.id > newId) {
                newId = detail.id
            }
        }

        return ++newId
    }
}