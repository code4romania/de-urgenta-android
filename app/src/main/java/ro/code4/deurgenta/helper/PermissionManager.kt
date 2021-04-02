package ro.code4.deurgenta.helper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class PermissionManager(private val activity: Activity, private val fragment: Fragment?) {

    private fun hasPermissions(vararg permissionString: String): Boolean = permissionString.all {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun isAnyDenied(vararg permissionString: String): Boolean = permissionString.any {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_DENIED
    }

    fun requestLocationRelatedPermissions(permissionsListener: PermissionListener) {
        try {

            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
            )

            val missingPermissions = mutableListOf<String>()
            for (permission in permissions) {
                if (!hasPermissions(permission)) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M && permission == Manifest.permission.CHANGE_NETWORK_STATE) {
                        // Exclude CHANGE_NETWORK_STATE as it does not require explicit user approval.
                        // This workaround is needed for devices running Android 6.0.0,
                        // see https://issuetracker.google.com/issues/37067994
                        continue
                    }
                    missingPermissions.add(permission)
                }
            }

            if (missingPermissions.isEmpty()) {
                permissionsListener.onPermissionsGranted()
            } else {
                checkIfDeniedOrRequest(missingPermissions.toTypedArray(), permissionsListener)
            }
        } catch (e: Exception) {
            Log.e(TAG, "exception retrieving permissions")
        }
    }


    fun checkPermissions(vararg permissionString: String, listener: PermissionListener) {
        if (hasPermissions(*permissionString)) {
            listener.onPermissionsGranted()
            return
        }
        checkIfDeniedOrRequest(permissionString, listener)
    }

    private fun checkIfDeniedOrRequest(
        permissionString: Array<out String>,
        listener: PermissionListener
    ) {
        if (isAnyDenied(*permissionString) && checkShouldShowRequestPermissionsRationale(*permissionString)) {
            listener.onPermissionDenied()
            return
        }
        requestPermissions(*permissionString)
    }

    fun requestPermissions(vararg permissionString: String) {

        if (fragment != null) {
            fragment.requestPermissions(permissionString, PERMISSION_REQUEST)
        } else {
            ActivityCompat.requestPermissions(activity, permissionString, PERMISSION_REQUEST)
        }
    }


    fun checkShouldShowRequestPermissionsRationale(vararg permissionString: String): Boolean {
        return permissionString.any {
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                it
            )
        }
    }

    interface PermissionListener {
        fun onPermissionsGranted()
        fun onPermissionDenied()
    }

    companion object {
        private const val TAG = "PermissionManager"

        @JvmStatic
        val PERMISSION_REQUEST = 320
    }

}