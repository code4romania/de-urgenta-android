package ro.code4.deurgenta.helper

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class PermissionManager(private val activity: Activity, private val fragment: Fragment?) {

    fun hasPermissions(vararg permissionString: String): Boolean = permissionString.all {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun isAnyDenied(vararg permissionString: String): Boolean = permissionString.any {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_DENIED
    }

    fun checkPermissions(listener: PermissionListener, permissionString: Array<String>) {
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
        checkPermissions(*permissionString)
    }

    private fun checkPermissions(vararg permissionString: String) {

        if (fragment != null) {
            fragment.requestPermissions(permissionString, PERMISSION_REQUEST)
        } else {
            ActivityCompat.requestPermissions(activity, permissionString, PERMISSION_REQUEST)
        }
    }

    fun checkShouldShowRequestPermissionsRationale(vararg permissionString: String): Boolean {
        return permissionString.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
    }

    interface PermissionListener {
        fun onPermissionsGranted()
        fun onPermissionDenied()
    }

    companion object {
        @JvmStatic
        val PERMISSION_REQUEST = 324

        @JvmStatic
        val PERMISSION_CHECK_SETTINGS = 320
    }

}