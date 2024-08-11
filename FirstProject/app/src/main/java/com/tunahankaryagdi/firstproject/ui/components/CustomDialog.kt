package com.tunahankaryagdi.firstproject.ui.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.DialogCustomBinding

class CustomDialog(
    private val onConfirm: () -> Unit,
) : DialogFragment(R.layout.dialog_custom) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogCustomBinding.inflate(LayoutInflater.from(requireContext()))

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