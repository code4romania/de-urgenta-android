package ro.code4.deurgenta.helper

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import ro.code4.deurgenta.R

fun showPermissionRationale(
    activity: Activity,
    isPermanentlyDenied: Boolean = false,
    permissionUiCallback: PermissionUiCallback
) {
    val (titleResId, message, positiveButton, positiveAction) = if (isPermanentlyDenied) {
        arrayOf(
            R.string.permission_permanently_denied_title,
            R.string.permission_permanently_denied_msg,
            R.string.permission_permanently_denied_settings_button,
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    openAppSettings(activity)
                }

            })
    } else {
        arrayOf(
            R.string.permission_denied_title,
            R.string.permission_denied_msg,
            R.string.permission_denied_ok_button,
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    permissionUiCallback.onDialogCallback()
                }
            })

    }

    AlertDialog.Builder(activity, R.style.AlertDialog)
        .setTitle(titleResId as Int)
        .setMessage(message as Int)
        .setNegativeButton(
            R.string.permission_denied_cancel_button
        ) { p0, _ -> p0.dismiss() }
        .setPositiveButton(
            positiveButton as Int,
            positiveAction as DialogInterface.OnClickListener
        )
        .show()

}

fun openAppSettings(activity: Activity) {
    activity.apply {
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY)
        )
    }
}

interface PermissionUiCallback {
    fun onDialogCallback()
}