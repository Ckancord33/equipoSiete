package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.model.Appointment
import com.example.dogapp.repository.AppointmentRepository
import kotlinx.coroutines.launch

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val appointmentRepository = AppointmentRepository(context)

    private val _listAppointment = MutableLiveData<MutableList<Appointment>>()
    val listAppointment: LiveData<MutableList<Appointment>> get() = _listAppointment

    private val _listBreeds = MutableLiveData<MutableList<String>>()
    val listBreeds: LiveData<MutableList<String>> get() = _listBreeds

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    fun saveAppointment(appointment: Appointment, onSaved: () -> Unit) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                val imageUrl = appointmentRepository.getDogImageByBreed(appointment.breed)
                val appointmentWithImage = appointment.copy(imageUrl = imageUrl)
                appointmentRepository.saveAppointment(appointmentWithImage)
                _progresState.value = false
                onSaved()
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getListAppointment() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listAppointment.value = appointmentRepository.getListAppointment()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getBreeds() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listBreeds.value = appointmentRepository.getBreeds()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun deleteAppointment(appointment: Appointment, onDeleted: () -> Unit) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.deleteAppointment(appointment)
                _progresState.value = false
                onDeleted()
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }
}
