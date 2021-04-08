package ro.code4.deurgenta.ui.backpack.edit

import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel
import java.util.*

class EditBackpackItemViewModel(
    private val repository: Repository
) : BaseViewModel() {

    fun saveNewItem(backpackId: String, type: BackpackItemType, name: String, quantity: Int, date: ExpirationDate) {
        val newId = UUID.randomUUID().toString()
        repository.saveNewBackpackItem(
            BackpackItem(
                newId, backpackId, name, quantity,
                ZonedDateTime.of(date.year, date.month, date.dayOfMonth, 0, 0, 0, 0, UTCZone),
                type
            )
        )
    }

    fun updateBackpackItem(
        targetBackpackItem: BackpackItem,
        name: String,
        quantity: Int,
        date: ExpirationDate
    ) {
        repository.updateBackpackItem(
            targetBackpackItem.copy(
                name = name,
                amount = quantity,
                expirationDate = ZonedDateTime.of(date.year, date.month, date.dayOfMonth, 0, 0, 0, 0, UTCZone)
            )
        )
    }

    companion object {
        private val UTCZone = ZoneId.of("UTC")
    }
}