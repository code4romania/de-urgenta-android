package ro.code4.deurgenta.ui.base

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.collapseKeyboardIfFocusOutsideEditText

abstract class BaseActivity<out T : BaseViewModel> : AppCompatActivity(), Layout,
    ViewModelSetter<T> {

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layout)

        viewModel.messageToast().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroy() {
        if (dialog != null && dialog?.isShowing == true) {
            dialog?.dismiss()
            dialog = null
        }
        super.onDestroy()
    }

    fun showDefaultErrorSnackBar(view: View) {
        showErrorSnackBar(view, getString(R.string.error_generic))
    }

    private fun showErrorSnackBar(view: View, text: String) {
        Snackbar.make(
            view,
            text,
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    //    Collapse the keyboard when the user taps outside the EditText
    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {

        currentFocus?.let { oldFocus ->
            super.dispatchTouchEvent(motionEvent)
            val newFocus = currentFocus ?: oldFocus
            collapseKeyboardIfFocusOutsideEditText(motionEvent, oldFocus, newFocus)
        }
        return super.dispatchTouchEvent(motionEvent)
    }
}