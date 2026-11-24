package com.example.dogapp.view.fragment

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentNewAppointmentBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.utils.Constants
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
        setupSymptomsSpinner()
        controladores()
        observerViewModel()
    }

    private fun setupToolbar() {
        binding.contentToolbar.tvToolbarTitle.text = getString(R.string.new_appointment_title)
        binding.contentToolbar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSymptomsSpinner() {
        val adapter = ArrayAdapter(
            binding.spSymptoms.context,
            android.R.layout.simple_spinner_item,
            Constants.SYMPTOMS_LIST
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSymptoms.adapter = adapter
    }

    private fun controladores() {
        actualizarEstadoBotonGuardar(false)
        validarDatos()
        binding.btnSaveAppointment.setOnClickListener {
            val selectedSymptom = binding.spSymptoms.selectedItem.toString()
            if (selectedSymptom == Constants.DEFAULT_SYMPTOM) {
                Toast.makeText(
                    context,
                    getString(R.string.new_appointment_select_symptom_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                saveAppointment()
            }
        }
    }

    private fun saveAppointment() {
        val appointment = Appointment(
            petName = binding.etPetName.text.toString().trim(),
            breed = binding.atBreed.text.toString().trim(),
            ownerName = binding.etOwnerName.text.toString().trim(),
            phone = binding.etPhone.text.toString().trim(),
            symptom = binding.spSymptoms.selectedItem.toString(),
            imageUrl = ""
        )
        binding.btnSaveAppointment.isEnabled = false
        appointmentViewModel.saveAppointment(appointment) {
            findNavController().popBackStack()
        }
    }

    private fun validarDatos() {
        val listEditText = listOf(
            binding.etPetName,
            binding.atBreed,
            binding.etOwnerName,
            binding.etPhone
        )
        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all { it.text.toString().trim().isNotEmpty() }
                actualizarEstadoBotonGuardar(isListFull)
            }
        }
    }

    private fun actualizarEstadoBotonGuardar(isEnabled: Boolean) {
        binding.btnSaveAppointment.isEnabled = isEnabled
        val bgColor = if (isEnabled) R.color.accent_pink else R.color.accent_pink_disabled
        val textColor = if (isEnabled) R.color.white else R.color.text_secondary
        binding.btnSaveAppointment.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), bgColor))
        binding.btnSaveAppointment.setTextColor(ContextCompat.getColor(requireContext(), textColor))
        binding.btnSaveAppointment.setTypeface(null, if (isEnabled) Typeface.BOLD else Typeface.NORMAL)
    }

    private fun observerViewModel() {
        observerListBreeds()
        observerProgress()
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

    private fun observerProgress() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status
        }
    }
}
