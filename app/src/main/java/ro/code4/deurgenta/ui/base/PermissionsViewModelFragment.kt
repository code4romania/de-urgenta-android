package ro.code4.deurgenta.ui.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import ro.code4.deurgenta.helper.PermissionManager
import ro.code4.deurgenta.helper.PermissionUiCallback
import ro.code4.deurgenta.helper.showPermissionRationale

abstract class PermissionsViewModelFragment<out T : BaseViewModel> : ViewModelFragment<T>(),
    PermissionManager.PermissionListener, PermissionUiCallback {

    private lateinit var permissionsUtils: PermissionManager

    override fun onResume() {
        super.onResume()
        if (!permissionsUtils.hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsUtils.checkPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        permissionsUtils = PermissionManager(requireActivity(), this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionManager.PERMISSION_REQUEST && grantResults.isNotEmpty()) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED } && grantResults.size == permissions.size) {
                onPermissionsGranted()
            } else {
                checkDeniedPermissions(permissions, grantResults)
            }
        }
    }

    private fun checkDeniedPermissions(permissions: Array<out String>, grantResults: IntArray) {
        val permanentlyDenied = permissions.filterIndexed { index, s ->
            grantResults[index] == PackageManager.PERMISSION_DENIED
                    && !permissionsUtils.checkShouldShowRequestPermissionsRationale(s)
        }

        if (permanentlyDenied.isNotEmpty()) {
            showPermissionRationale(requireActivity(), true, this)
        } else {
            val denied = permissions.filterIndexed { index, s ->
                grantResults[index] == PackageManager.PERMISSION_DENIED
                        && permissionsUtils.checkShouldShowRequestPermissionsRationale(s)
            }
            if (denied.isNotEmpty()) {
                showPermissionRationale(requireActivity(), true, this)
            }
        }
    }

    override fun onDialogCallback() {
        permissionsUtils.checkPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    override fun onPermissionsGranted() = Unit

    override fun onPermissionDenied() = Unit
}