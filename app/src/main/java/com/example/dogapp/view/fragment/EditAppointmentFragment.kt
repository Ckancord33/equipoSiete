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
import com.example.dogapp.databinding.FragmentEditAppointmentBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.viewmodel.AppointmentViewModel

class EditAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentEditAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private var appointment: Appointment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appointment = arguments?.getSerializable("clave") as? Appointment
        setupToolbar()
        precargarDatos()
        controladores()
        observerViewModel()
    }

    private fun setupToolbar() {
        binding.contentToolbar.tvToolbarTitle.text = getString(R.string.edit_appointment_title)
        binding.contentToolbar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun precargarDatos() {
        appointment?.let { item ->
            binding.etPetName.setText(item.petName)
            binding.atBreed.setText(item.breed, false)
            binding.etOwnerName.setText(item.ownerName)
            binding.etPhone.setText(item.phone)
        }
    }

    private fun controladores() {
        validarDatos()
        binding.btnEditAppointment.setOnClickListener {
            updateAppointment()
        }
    }

    private fun updateAppointment() {
        val currentAppointment = appointment ?: return
        val newPetName = binding.etPetName.text.toString().trim()
        val newBreed = binding.atBreed.text.toString().trim()
        val newOwnerName = binding.etOwnerName.text.toString().trim()
        val newPhone = binding.etPhone.text.toString().trim()
        val breedChanged = !newBreed.equals(currentAppointment.breed, ignoreCase = true)

        val updatedAppointment = currentAppointment.copy(
            petName = newPetName,
            breed = newBreed,
            ownerName = newOwnerName,
            phone = newPhone
        )
        binding.btnEditAppointment.isEnabled = false
        appointmentViewModel.updateAppointment(updatedAppointment, breedChanged) {
            Toast.makeText(
                context,
                getString(R.string.edit_appointment_success),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack(R.id.homeFragment, false)
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
                actualizarEstadoBotonEditar(isListFull)
            }
        }
        val initialFull = listEditText.all { it.text.toString().trim().isNotEmpty() }
        actualizarEstadoBotonEditar(initialFull)
    }

    private fun actualizarEstadoBotonEditar(isEnabled: Boolean) {
        binding.btnEditAppointment.isEnabled = isEnabled
        val bgColor = if (isEnabled) R.color.accent_pink else R.color.accent_pink_disabled
        val textColor = if (isEnabled) R.color.white else R.color.text_secondary
        binding.btnEditAppointment.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), bgColor))
        binding.btnEditAppointment.setTextColor(ContextCompat.getColor(requireContext(), textColor))
        binding.btnEditAppointment.setTypeface(null, if (isEnabled) Typeface.BOLD else Typeface.NORMAL)
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
