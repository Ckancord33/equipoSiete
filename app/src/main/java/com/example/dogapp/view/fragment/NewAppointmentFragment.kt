package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentNewAppointmentBinding
import com.example.dogapp.viewmodel.AppointmentViewModel

class NewAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentNewAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observerViewModel()
    }

    private fun setupToolbar() {
        binding.contentToolbar.tvToolbarTitle.text = getString(R.string.new_appointment_title)
        binding.contentToolbar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observerViewModel() {
        observerListBreeds()
    }

    private fun observerListBreeds() {
        appointmentViewModel.getBreeds()
        appointmentViewModel.listBreeds.observe(viewLifecycleOwner) { listBreeds ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listBreeds
            )
            binding.atBreed.setAdapter(adapter)
        }
    }
}
