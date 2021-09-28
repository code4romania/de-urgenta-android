package ro.code4.deurgenta.ui.backpack.main

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ro.code4.deurgenta.R

class AddBackpackDialogFragment : DialogFragment() {

    private val viewModel: BackpacksViewModel by sharedViewModel()
    private lateinit var inputView: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_new_backpack, null)
        inputView = dialogView.findViewById(R.id.input_new_backpack_name)
        inputView.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // no-op
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { editable ->
                    if (editable.isNotEmpty() && editable.toString().toCharArray().any { it.isLetterOrDigit() }) {
                        enableActionButton()
                    } else {
                        enableActionButton(false)
                    }
                }
            }
        })
        return MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_shopping_bag)
            .setTitle(R.string.backpack_new_name_dialog_title)
            .setView(dialogView)
            .setNegativeButton(R.string.backpack_new_cancel) { _, _ -> dismiss() }
            .setPositiveButton(R.string.backpack_new_ok) { _: DialogInterface, _: Int ->
                viewModel.saveNewBackpack(inputView.text.trim().toString())
            }
            .create()
    }

    override fun onResume() {
        super.onResume()
        if (inputView.text.isEmpty()) {
            enableActionButton(false)
        }
    }

    private fun enableActionButton(disable: Boolean = true) {
        (dialog as? AlertDialog)?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = disable
    }
}