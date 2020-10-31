package com.utn.frba.desarrollomobile.hunter.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.utn.frba.desarrollomobile.hunter.R

fun AppCompatActivity.showFragment(
    fragment: Fragment,
    addToBackStack: Boolean,
    clearStack: Boolean = false
) {
    doShowFragment(supportFragmentManager, fragment, addToBackStack, clearStack)
}

fun Fragment.showFragment(
    fragment: Fragment,
    addToBackStack: Boolean,
    clearStack: Boolean = false
) {
    doShowFragment(parentFragmentManager, fragment, addToBackStack, clearStack)
}

private fun doShowFragment(
    fragmentManager: FragmentManager,
    fragment: Fragment,
    addToBackStack: Boolean,
    clearStack: Boolean
) {

    val transaction = fragmentManager.beginTransaction()
    transaction.setCustomAnimations(
        R.anim.slide_in_right_no_alpha,
        R.anim.slide_out_left_no_alpha,
        R.anim.slide_in_left_no_alpha,
        R.anim.slide_out_right_no_alpha
    )
    transaction.replace(R.id.fragment_container, fragment)
    if (addToBackStack) {
        transaction.addToBackStack(null)
    }
    if (clearStack) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
    transaction.commitAllowingStateLoss()
}

fun Fragment.removeFragment() {
    parentFragmentManager.findFragmentById(R.id.fragment_container)?.let {
        parentFragmentManager.beginTransaction().remove(it).commit()
        parentFragmentManager.popBackStack()
    }
}