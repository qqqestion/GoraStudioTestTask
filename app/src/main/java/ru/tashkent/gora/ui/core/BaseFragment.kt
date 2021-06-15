package ru.tashkent.gora.ui.core

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    fun snackbar(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

    fun snackbar(@StringRes strId: Int) {
        Snackbar.make(requireView(), strId, Snackbar.LENGTH_SHORT).show()
    }
}