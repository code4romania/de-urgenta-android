package ro.code4.deurgenta.helper

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson

fun Activity.startActivityWithoutTrace(activity: Class<*>) {
    startActivity(Intent(this, activity))
    finishAffinity()
}

fun Fragment.startActivityWithoutTrace(activity: Class<*>) {
    startActivity(Intent(this.activity, activity))
    this.activity?.finishAffinity()
}

fun FragmentManager.replaceFragment(
    @IdRes layoutRes: Int, fragment: Fragment,
    bundle: Bundle? = null,
    tag: String? = null,
    isPrimaryNavigationFragment: Boolean = false
) {
    val ft = beginTransaction()
    if (isPrimaryNavigationFragment) {
        ft.setPrimaryNavigationFragment(fragment)
    }
    bundle?.let {
        fragment.arguments = it
    }
    tag?.let {
        ft.addToBackStack(it)
    }
    ft.replace(layoutRes, fragment, tag)

    ft.commit()
}

fun Context.isOnline(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            //It will check for both wifi and cellular network
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    } else {
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}

fun <T> String.fromJson(gson: Gson, clazz: Class<T>): T {
    return gson.fromJson(this, clazz)
}

fun String.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.isEmptyField() = isNullOrBlank()

/*
 *  Hide software keyboard if user taps outside the EditText
 *  use inside override fun dispatchTouchEvent()
 */
fun collapseKeyboardIfFocusOutsideEditText(
    motionEvent: MotionEvent,
    oldFocusedView: View,
    newFocusedView: View
) {
    if (motionEvent.action == MotionEvent.ACTION_UP) {
        if (newFocusedView == oldFocusedView) {

            val srcCoordinates = IntArray(2)
            oldFocusedView.getLocationOnScreen(srcCoordinates)

            val rect = Rect(
                srcCoordinates[0], srcCoordinates[1], srcCoordinates[0] +
                        oldFocusedView.width, srcCoordinates[1] + oldFocusedView.height
            )

            if (rect.contains(motionEvent.x.toInt(), motionEvent.y.toInt()))
                return
        } else if (newFocusedView is EditText) {
            //  If new focus is other EditText then will not collapse
            return
        }

        // Collapse the keyboard from activity
        ContextCompat.getSystemService(newFocusedView.context, InputMethodManager::class.java)
            ?.hideSoftInputFromWindow(newFocusedView.windowToken, 0)
    }
}

fun ImageView.setToRotateIndefinitely(): ObjectAnimator =
    ObjectAnimator.ofFloat(this, "rotation", 0f, 360f).apply {
        repeatCount = ObjectAnimator.INFINITE
        duration = 2000
        interpolator = LinearInterpolator()
        repeatMode = ObjectAnimator.RESTART
    }

fun hideSoftInput(view: View) {
    val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
