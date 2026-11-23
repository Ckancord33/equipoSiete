package com.example.dogapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogapp.databinding.ItemAppointmentBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.view.viewholder.AppointmentViewHolder

class AppointmentAdapter(private val listAppointment: MutableList<Appointment>) :
    RecyclerView.Adapter<AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listAppointment.size
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = listAppointment[position]
        holder.setItemAppointment(appointment)
    }
}
