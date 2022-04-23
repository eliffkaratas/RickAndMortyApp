package com.example.rickandmortyapp.util

import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(messageToShow: String) {
    Toast.makeText(requireContext(), messageToShow, Toast.LENGTH_LONG).show()
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

fun View.setFullHeight(view: View): View {
    val layoutParams = view.layoutParams
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    view.layoutParams = layoutParams
    return this
}
