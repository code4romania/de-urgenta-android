package ro.code4.deurgenta.ui.backpack.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.helper.SchedulersProvider
import ro.code4.deurgenta.helper.SchedulersProviderImpl
import ro.code4.deurgenta.helper.logE
import ro.code4.deurgenta.helper.logI
import ro.code4.deurgenta.helper.plusAssign
import ro.code4.deurgenta.repositories.BackpacksRepository
import ro.code4.deurgenta.ui.base.BaseViewModel
import java.time.ZoneId
import java.time.ZonedDateTime

class EditBackpackItemViewModel(
    private val repository: BackpacksRepository,
    private val schedulersProvider: SchedulersProvider = SchedulersProviderImpl()
) : BaseViewModel() {

    private val _requestStatus = MutableLiveData<BackpackItemEditStatus>()
    val requestStatus: LiveData<BackpackItemEditStatus> = _requestStatus

    init {
        _requestStatus.value = BackpackItemEditStatus.NotInitiated
    }

    fun saveNewItem(backpackId: String, type: BackpackItemType, name: String, quantity: Int, date: ExpirationDate?) {
        _requestStatus.value = BackpackItemEditStatus.InProgress
        val newItem = BackpackItem(
            id = "",
            backpackId = backpackId,
            name = name,
            amount = quantity,
            expirationDate = date?.parse(),
            type = type,
            version = 1
        )
        disposables += repository
            .saveNewBackpackItem(newItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribeOn(Schedulers.io()).subscribe({
                logI("saveNewBackpackItem($newItem): Success!")
                _requestStatus.value = BackpackItemEditStatus.Succeeded
            }, {
                logE("saveNewBackpackItem($newItem): Error($it)!")
                _requestStatus.value = BackpackItemEditStatus.Failed
            })
    }

    fun updateBackpackItem(backpackItem: BackpackItem, name: String, quantity: Int, date: ExpirationDate?) {
        _requestStatus.value = BackpackItemEditStatus.InProgress
        val itemToBeUpdated = backpackItem.copy(
            name = name,
            amount = quantity,
            expirationDate = date?.parse()
        )
        disposables += repository
            .updateBackpackItem(itemToBeUpdated)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribeOn(Schedulers.io()).subscribe({
                logI("updateBackpackItem($itemToBeUpdated): Success!")
                _requestStatus.value = BackpackItemEditStatus.Succeeded
            }, {
                logE("updateBackpackItem($itemToBeUpdated): Error($it)!")
                _requestStatus.value = BackpackItemEditStatus.Failed
            })
    }

    companion object {
        private val UTCZone = ZoneId.of("Europe/Bucharest")
    }

    private fun ExpirationDate?.parse(): ZonedDateTime? {
        return if (this == null) {
            null
        } else {
            val hour = 0
            @Suppress("MagicNumber")
            val minute = 30 // a convention to start the day half an hour after midnight
            val second = 0
            ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, 0, UTCZone)
        }
    }
}

enum class BackpackItemEditStatus {
    NotInitiated, InProgress, Succeeded, Failed
}
