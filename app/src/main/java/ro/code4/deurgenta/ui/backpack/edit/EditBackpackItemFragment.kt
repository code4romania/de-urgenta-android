package ro.code4.deurgenta.ui.backpack.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_backpack_edit_item.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.parceler.Parcels
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.ui.base.ViewModelFragment
import java.text.SimpleDateFormat
import java.util.*

class EditBackpackItemFragment : ViewModelFragment<EditBackpackItemViewModel>() {
    override val screenName: Int
        get() = R.string.app_name
    override val layout: Int
        get() = R.layout.fragment_backpack_edit_item
    override val viewModel: EditBackpackItemViewModel by viewModel()
    private var expirationDate: ExpirationDate = LocalDate.now().let {
        ExpirationDate(it.year, it.monthValue, it.dayOfMonth)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_cancel_add_item.setOnClickListener { findNavController().popBackStack() }
        input_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    input_date.text = formatter.format(calendar.time)
                    expirationDate = ExpirationDate(year, month + 1, dayOfMonth)
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val requestType = arguments?.getInt(KEY_REQUEST_TYPE) ?: error(
            "The edit fragment must have a valid request type"
        )
        when (requestType) {
            TYPE_NEW_ITEM -> handleAddNewItem()
            TYPE_EDIT_ITEM -> handleUpdateItem()
            else -> error("Unknown request type: $requestType")
        }
    }

    private fun handleAddNewItem() {
        btn_change_action.text = getString(R.string.btn_item_change_new)
        btn_change_action.setOnClickListener {
            val newItemData: NewItemData = Parcels.unwrap(arguments?.getParcelable(KEY_NEW_ITEM_DATA))
            val nameInput = input_name.text.toString()
            val quantityInput = input_quantity.text.toString()
            if (isInputValid(nameInput, quantityInput)) {
                viewModel.saveNewItem(
                    newItemData.backpackId,
                    newItemData.backpackItemType,
                    nameInput, quantityInput.toInt(), expirationDate
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun handleUpdateItem() {
        btn_change_action.text = getString(R.string.btn_item_change_update)
        val backpackItem: BackpackItem = Parcels.unwrap(arguments?.getParcelable(KEY_EDIT_ITEM_DATA))
        input_name.setText(backpackItem.name)
        input_quantity.setText(backpackItem.amount.toString())
        input_date.text = displayFormatter.format(backpackItem.expirationDate)
        expirationDate = ExpirationDate(
            backpackItem.expirationDate.year, backpackItem.expirationDate.monthValue,
            backpackItem.expirationDate.dayOfMonth
        )
        btn_change_action.setOnClickListener {
            val name = input_name.text.toString()
            val quantity = input_quantity.text.toString()
            if (isInputValid(name, quantity)) {
                viewModel.updateBackpackItem(backpackItem, name, quantity.toInt(), expirationDate)
                findNavController().popBackStack()
            }
        }
    }

    private fun isInputValid(name: String, quantity: String): Boolean {
        var checkOutcome = true
        val referenceDate = LocalDate.now()
        val selected = LocalDate.of(expirationDate.year, expirationDate.month, expirationDate.dayOfMonth)
        if (selected.isBefore(referenceDate) || selected.isEqual(referenceDate)) {
            input_date_error.text = getString(R.string.edit_backpack_item_error_date)
            checkOutcome = false
        } else {
            input_date_error.text = ""
        }
        if (name.isEmpty()) {
            input_name_layout.error = getString(R.string.edit_backpack_item_error_name)
            checkOutcome = false
        } else {
            input_name_layout.error = ""
        }
        if (quantity.isEmpty()) {
            input_quantity_layout.error = getString(R.string.edit_backpack_item_error_quantity)
            checkOutcome = false
        } else {
            input_quantity_layout.error = ""
        }
        if (kotlin.runCatching { quantity.toInt() }.isFailure) {
            input_quantity_layout.error = getString(R.string.edit_backpack_item_error_quantity)
            checkOutcome = false
        } else {
            input_quantity_layout.error = ""
        }
        return checkOutcome
    }

    companion object {
        const val KEY_REQUEST_TYPE = "key_request_type"
        const val TYPE_NEW_ITEM = 1
        const val TYPE_EDIT_ITEM = 2

        const val KEY_NEW_ITEM_DATA = "key_new_item_data"
        const val KEY_EDIT_ITEM_DATA = "key_edit_item_data"

        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        val displayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
    }
}

@Parcelize
data class NewItemData(val backpackId: String, val backpackItemType: BackpackItemType) : Parcelable

data class ExpirationDate(val year: Int, val month: Int, val dayOfMonth: Int)
