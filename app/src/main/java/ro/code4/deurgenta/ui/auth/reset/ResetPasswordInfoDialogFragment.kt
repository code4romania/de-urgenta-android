package ro.code4.deurgenta.ui.auth.reset

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import androidx.transition.TransitionManager
import org.koin.android.viewmodel.ext.android.getViewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.Status.Error
import ro.code4.deurgenta.helper.Status.Loading
import ro.code4.deurgenta.helper.Status.Success

class ResetPasswordInfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                // don't allow the user to cancel the dialog with the BACK button
                // the request will succeed or fail and then he will have the opportunity to act
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.dialog_reset_password_info_loading, container, false)
        contentView.findViewById<Button>(R.id.btn_reset_action).setOnClickListener {
            requireParentFragment().parentFragmentManager.popBackStack()
        }
        val constraintLayout = contentView.findViewById<ConstraintLayout>(R.id.container)
        val infoView = contentView.findViewById<TextView>(R.id.infoView)
        val resetAction = contentView.findViewById<Button>(R.id.btn_reset_action)
        val successConstraints = ConstraintSet().apply {
            clone(requireContext(), R.layout.dialog_reset_password_info_success)
        }
        val failureConstraints = ConstraintSet().apply {
            clone(requireContext(), R.layout.dialog_reset_password_info_failure)
        }
        requireParentFragment().getViewModel<ResetPasswordViewModel>()
            .resetStatus.observe(viewLifecycleOwner) { event ->
                when (event.status) {
                    Loading -> {
                        infoView.text = getString(R.string.reset_password_loading)
                    }
                    Success -> {
                        infoView.text = getString(R.string.reset_password_success)
                        resetAction.text = getString(R.string.reset_password_dialog_btn_success)
                        resetAction.setOnClickListener { requireParentFragment().parentFragmentManager.popBackStack() }
                        TransitionManager.beginDelayedTransition(constraintLayout)
                        successConstraints.applyTo(constraintLayout)
                    }
                    Error -> {
                        infoView.text = getString(R.string.reset_password_failure)
                        resetAction.text = getString(R.string.reset_password_dialog_btn_failure)
                        resetAction.setOnClickListener { dismiss() }
                        TransitionManager.beginDelayedTransition(constraintLayout)
                        failureConstraints.applyTo(constraintLayout)
                    }
                }
            }
        return contentView
    }
}