package com.tunahankaryagdi.firstproject.ui.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.CustomDialogBinding

class CustomDialog(
    private val onConfirm: () -> Unit,
) : DialogFragment(R.layout.custom_dialog) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = CustomDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val builder = AlertDialog.Builder(requireContext()).setView(binding.root)

        binding.tvDialogConfirm.setOnClickListener {
            onConfirm()
            dismiss()
        }

        binding.tvDialogCancel.setOnClickListener {
            dismiss()
        }
        return builder.create()
    }
}