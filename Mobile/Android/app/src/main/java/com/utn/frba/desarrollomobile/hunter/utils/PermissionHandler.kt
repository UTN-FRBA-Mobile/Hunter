package com.utn.frba.desarrollomobile.hunter.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionHandler {

    fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.all { permission ->
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PERMISSION_GRANTED
            }
        } else {
            true
        }
    }

    fun requestPermissions(
        fragment: Fragment,
        permissions: Array<String>,
        permissionsCode: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragment.requestPermissions(permissions, permissionsCode)
        }
    }

    //in case user checked "don't ask again"
    fun isPermissionDeniedForever(activity: AppCompatActivity, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            !activity.shouldShowRequestPermissionRationale(permission)
        } else {
            false
        }
    }

}