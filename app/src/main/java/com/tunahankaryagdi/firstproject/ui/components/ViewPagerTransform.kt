package com.tunahankaryagdi.firstproject.ui.components

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ViewPagerTransform : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val scale = 1 - Math.abs(position)
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = scale
    }
}