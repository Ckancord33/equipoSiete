package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentDetailBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.viewmodel.AppointmentViewModel

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private var appointment: Appointment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appointment = arguments?.getSerializable("clave") as? Appointment
        setupToolbar()
        cargarDetalleCita()
        controladores()
        observerViewModel()
    }

    private fun setupToolbar() {
        binding.contentToolbar.tvToolbarTitle.text = appointment?.petName ?: ""
        binding.contentToolbar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun cargarDetalleCita() {
        appointment?.let { item ->
            binding.tvDetailTurn.text = getString(R.string.home_turn_format, item.id)
            binding.tvDetailBreed.text = item.breed
            binding.tvDetailSymptom.text = item.symptom
            binding.tvDetailOwner.text = item.ownerName
            binding.tvDetailPhone.text = item.phone

            Glide.with(this)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_dog_header_placeholder)
                .error(R.drawable.ic_dog_header_placeholder)
                .into(binding.ivDetailPetPhoto)
        }
    }

    private fun controladores() {
        binding.fabDeleteAppointment.setOnClickListener {
            mostrarDialogoEliminar()
        }
        binding.fabEditAppointment.setOnClickListener {
            editarCita()
        }
    }

    private fun mostrarDialogoEliminar() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.detail_delete_dialog_title))
            .setMessage(getString(R.string.detail_delete_dialog_message))
            .setPositiveButton(getString(R.string.detail_delete_dialog_confirm)) { dialog, _ ->
                appointment?.let { currentAppointment ->
                    appointmentViewModel.deleteAppointment(currentAppointment) {
                        Toast.makeText(
                            context,
                            getString(R.string.detail_delete_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.detail_delete_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun editarCita() {
        appointment?.let { item ->
            val bundle = Bundle()
            bundle.putSerializable("clave", item)
            findNavController().navigate(R.id.action_detailFragment_to_editAppointmentFragment, bundle)
        }
    }

    private fun observerViewModel() {
        observerProgress()
    }

    private fun observerProgress() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status
        }
    }
}
