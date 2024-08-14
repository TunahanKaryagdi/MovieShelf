package com.tunahankaryagdi.firstproject.ui.components

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.CustomToastBinding

class CustomToast(private val context: Context) {

    fun show(message: String,iconId: Int? = null) {

        val inflater = LayoutInflater.from(context)
        val binding = CustomToastBinding.inflate(inflater)
        binding.tvToastMessage.text = message
        if (iconId == null){
            binding.ivToastIcon.setImageResource(R.drawable.ic_successfully)
        }
        else{
            binding.ivToastIcon.setImageResource(iconId)
        }
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = binding.root
        toast.show()
    }
}