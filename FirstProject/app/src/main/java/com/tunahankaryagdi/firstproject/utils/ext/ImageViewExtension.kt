package com.tunahankaryagdi.firstproject.utils.ext

import android.widget.ImageView
import coil.load
import com.tunahankaryagdi.firstproject.R


fun ImageView.loadImage(imageUrl: String?, resId: Int = R.drawable.bg_rounded_corner_small) {
    if (imageUrl != null) {
        this.load(imageUrl)
    }
    else{
        this.setImageResource(resId)
    }
}