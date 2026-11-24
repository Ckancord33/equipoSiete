package com.example.dogapp.repository

import android.content.Context
import com.example.dogapp.data.AppointmentDB
import com.example.dogapp.data.AppointmentDao
import com.example.dogapp.model.Appointment
import com.example.dogapp.webservice.ApiService
import com.example.dogapp.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppointmentRepository(val context: Context) {
    private var appointmentDao: AppointmentDao = AppointmentDB.getDatabase(context).appointmentDao()
    private var apiService: ApiService = ApiUtils.getApiService()

    suspend fun saveAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.saveAppointment(appointment)
        }
    }

    suspend fun getListAppointment(): MutableList<Appointment> {
        return withContext(Dispatchers.IO) {
            appointmentDao.getListAppointment()
        }
    }

    suspend fun deleteAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.deleteAppointment(appointment)
        }
    }

    suspend fun updateAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.updateAppointment(appointment)
        }
    }

    suspend fun getBreeds(): MutableList<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getBreeds()
                response.message.keys.toMutableList()
            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        }
    }

    suspend fun getDogImageByBreed(breed: String): String {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getBreedImage(breed.trim().lowercase()).message
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}
