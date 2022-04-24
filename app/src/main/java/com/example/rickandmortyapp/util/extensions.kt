package com.example.rickandmortyapp.util

import android.app.Application
import android.view.View
import android.widget.Toast

fun Application.showToast(messageToShow: String) {
    Toast.makeText(applicationContext, messageToShow, Toast.LENGTH_LONG).show()
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}
