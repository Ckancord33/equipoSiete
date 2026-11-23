package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dogapp.databinding.FragmentHomeBinding
import com.example.dogapp.view.adapter.AppointmentAdapter
import com.example.dogapp.viewmodel.AppointmentViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        observerViewModel()
    }

    private fun controladores() {
        binding.fabAddAppointment.setOnClickListener {
            // TODO: Navigate to New Appointment screen (HU 3.0)
        }
    }

    private fun observerViewModel() {
        observerListAppointment()
        observerProgress()
    }

    private fun observerListAppointment() {
        appointmentViewModel.getListAppointment()
        appointmentViewModel.listAppointment.observe(viewLifecycleOwner) { listAppointment ->
            val recycler = binding.rvAppointments
            val adapter = AppointmentAdapter(listAppointment)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun observerProgress() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status
        }
    }
}
