package com.utn.frba.desarrollomobile.hunter.ui.customviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utn.frba.desarrollomobile.hunter.R
import kotlinx.android.synthetic.main.loading_layout.view.*

class LoadingView private constructor(view: ViewGroup, context: Context) {

    private var mParentView: ViewGroup = view
    private var mContext: Context = context
    private var isShowing: Boolean = false

    companion object {

        fun create(viewGroup: ViewGroup, context: Context): LoadingView {
            return LoadingView(viewGroup, context)
        }
    }

    fun show(message: String = "") {
        isShowing = true
        var view = mParentView.loadingRoot
        if (view == null) {
            val overlay =
                LayoutInflater.from(mContext).inflate(R.layout.loading_layout, mParentView, false)
            mParentView.addView(overlay)
            view = mParentView.loadingRoot
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.VISIBLE
        }
        resetView()
        mParentView.loadingText.text = message
    }

    fun dismiss() {
        isShowing = false
        resetView()
        val view = mParentView.loadingRoot
        if (view != null) {
            view.visibility = View.GONE
        }
    }

    //Elimina el texto de mensaje
    private fun resetView() {
        val view = mParentView.loadingRoot
        if (view != null) {
            view.loadingText.text = ""
        }
    }
}