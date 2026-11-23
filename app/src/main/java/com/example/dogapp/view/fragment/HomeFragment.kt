package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            // TODO (HU 3.0 - New Appointment): once the new appointment fragment and the action
            //  "action_homeFragment_to_newAppointmentFragment" exist in nav_graph.xml, uncomment the line below.
            // findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
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
            val adapter = AppointmentAdapter(listAppointment, findNavController())
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
