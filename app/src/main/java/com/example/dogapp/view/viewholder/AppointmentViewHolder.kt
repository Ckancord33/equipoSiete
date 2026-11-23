package com.example.dogapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemAppointmentBinding
import com.example.dogapp.model.Appointment

class AppointmentViewHolder(binding: ItemAppointmentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding

    fun setItemAppointment(appointment: Appointment) {
        bindingItem.tvPetName.text = appointment.petName
        bindingItem.tvPetSymptom.text = appointment.symptom
        bindingItem.tvAppointmentTurn.text =
            bindingItem.root.context.getString(R.string.home_turn_format, appointment.id)
    }
}
