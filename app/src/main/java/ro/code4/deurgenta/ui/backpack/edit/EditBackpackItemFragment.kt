package ro.code4.deurgenta.ui.backpack.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.databinding.FragmentBackpackEditItemBinding
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.ui.base.BaseFragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class EditBackpackItemFragment : BaseFragment(R.layout.fragment_backpack_edit_item) {
    override val screenName: Int
        get() = R.string.app_name
    private val viewModel: EditBackpackItemViewModel by viewModel()
    private val binding: FragmentBackpackEditItemBinding by viewBinding(FragmentBackpackEditItemBinding::bind)
    private var expirationDate: ExpirationDate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancelAddItem.setOnClickListener { findNavController().popBackStack() }
        binding.inputDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    binding.inputDate.text = formatter.format(calendar.time)
                    expirationDate = ExpirationDate(year, month + 1, dayOfMonth)
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val requestType = arguments?.getInt(KEY_REQUEST_TYPE) ?: error(
            "A request type must be provided to edit a backpack item content!"
        )
        when (requestType) {
            TYPE_NEW_ITEM -> handleAddNewItem()
            TYPE_EDIT_ITEM -> handleUpdateItem()
            else -> error("Unknown request type: $requestType")
        }

        viewModel.requestStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                BackpackItemEditStatus.NotInitiated -> {
                    // default ui
                }
                BackpackItemEditStatus.InProgress -> {
                    binding.loadingIndicator.apply {
                        visibility = View.VISIBLE
                        setToRotateIndefinitely().start()
                    }
                    updateUiAvailability(shouldBeAvailable = false)
                }
                BackpackItemEditStatus.Succeeded -> {
                    binding.loadingIndicator.visibility = View.GONE
                    updateUiAvailability(shouldBeAvailable = true)
                    findNavController().popBackStack()
                }
                BackpackItemEditStatus.Failed -> {
                    binding.loadingIndicator.visibility = View.GONE
                    updateUiAvailability(shouldBeAvailable = true)
                    showInfoSnack()
                }
                else -> {
                    /* ignored, null Java enum */
                }
            }
        }
    }

    private fun updateUiAvailability(shouldBeAvailable: Boolean) {
        binding.btnCancelAddItem.isEnabled = shouldBeAvailable
        binding.btnChangeAction.isEnabled = shouldBeAvailable
    }

    private fun showInfoSnack() {
        val requestType = arguments?.getInt(KEY_REQUEST_TYPE) ?: error(
            "A request type must be provided to edit a backpack item content!"
        )
        val message = when (requestType) {
            TYPE_NEW_ITEM -> getString(R.string.edit_backpack_error_new_item)
            TYPE_EDIT_ITEM -> getString(R.string.edit_backpack_error_update_item)
            else -> error("Unknown request type: $requestType")
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun handleAddNewItem() {
        binding.btnChangeAction.text = getString(R.string.btn_item_change_new)
        binding.btnChangeAction.setOnClickListener {
            val newItemData: NewItemData = arguments?.getParcelable(KEY_NEW_ITEM_DATA)
                ?: error("Must provided a backpack id and type of item to add new items!")
            val nameInput = binding.inputName.text.toString()
            val quantityInput = binding.inputQuantity.text.toString()
            if (isInputValid(nameInput, quantityInput)) {
                viewModel.saveNewItem(
                    newItemData.backpackId,
                    newItemData.backpackItemType,
                    nameInput, quantityInput.toInt(), expirationDate
                )
            }
        }
    }

    private fun handleUpdateItem() {
        binding.btnChangeAction.text = getString(R.string.btn_item_change_update)
        val backpackItem: BackpackItem = arguments?.getParcelable(KEY_EDIT_ITEM_DATA)
            ?: error("Must provided a backpack id and type of item to change an item!")
        binding.inputName.setText(backpackItem.name)
        binding.inputQuantity.setText(backpackItem.amount.toString())
        backpackItem.expirationDate?.let {
            binding.inputDate.text = displayFormatter.format(backpackItem.expirationDate)
            expirationDate = ExpirationDate(it.year, it.monthValue, it.dayOfMonth)
        }
        binding.btnChangeAction.setOnClickListener {
            val name = binding.inputName.text.toString()
            val quantity = binding.inputQuantity.text.toString()
            if (isInputValid(name, quantity)) {
                viewModel.updateBackpackItem(backpackItem, name, quantity.toInt(), expirationDate)
            }
        }
    }

    private fun isInputValid(name: String, quantity: String): Boolean {
        var checkOutcome = true
        val referenceDate = LocalDate.now()
        val selected = expirationDate?.let { LocalDate.of(it.year, it.month, it.dayOfMonth) }
        if (selected != null && (selected.isBefore(referenceDate) || selected.isEqual(referenceDate))) {
            binding.inputDateError.text = getString(R.string.edit_backpack_item_error_date)
            checkOutcome = false
        } else {
            binding.inputDateError.text = ""
        }
        if (name.isEmpty()) {
            binding.inputNameLayout.error = getString(R.string.edit_backpack_item_error_name)
            checkOutcome = false
        } else {
            binding.inputNameLayout.error = ""
        }
        if (quantity.isEmpty()) {
            binding.inputQuantityLayout.error = getString(R.string.edit_backpack_item_error_quantity)
            checkOutcome = false
        } else {
            binding.inputQuantityLayout.error = ""
        }
        if (kotlin.runCatching { quantity.toInt() }.isFailure) {
            binding.inputQuantityLayout.error = getString(R.string.edit_backpack_item_error_quantity)
            checkOutcome = false
        } else {
            binding.inputQuantityLayout.error = ""
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
        private val UTCZone = ZoneId.of("Europe/Bucharest")
        val displayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
            .withZone(UTCZone)
    }
}

@Parcelize
data class NewItemData(val backpackId: String, val backpackItemType: BackpackItemType) : Parcelable

data class ExpirationDate(val year: Int, val month: Int, val dayOfMonth: Int)
