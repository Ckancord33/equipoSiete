package com.example.dogapp.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemAppointmentBinding
import com.example.dogapp.model.Appointment

class AppointmentViewHolder(binding: ItemAppointmentBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding
    val navController = navController

    fun setItemAppointment(appointment: Appointment) {
        bindingItem.tvPetName.text = appointment.petName
        bindingItem.tvPetSymptom.text = appointment.symptom
        bindingItem.tvAppointmentTurn.text =
            bindingItem.root.context.getString(R.string.home_turn_format, appointment.id)
        Glide.with(bindingItem.root.context)
            .load(appointment.imageUrl)
            .placeholder(R.drawable.ic_dog_header_placeholder)
            .error(R.drawable.ic_dog_header_placeholder)
            .into(bindingItem.ivPetPhoto)

        bindingItem.cvAppointment.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", appointment)
            navController.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }
}
