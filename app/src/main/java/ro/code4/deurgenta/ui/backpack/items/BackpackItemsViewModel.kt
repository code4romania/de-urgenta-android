package ro.code4.deurgenta.ui.backpack.items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.Disposable
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.helper.SchedulersProvider
import ro.code4.deurgenta.helper.SchedulersProviderImpl
import ro.code4.deurgenta.helper.logE
import ro.code4.deurgenta.helper.logI
import ro.code4.deurgenta.helper.plusAssign
import ro.code4.deurgenta.repositories.BackpacksRepository
import ro.code4.deurgenta.ui.base.BaseViewModel

class BackpackItemsViewModel(
    private val repository: BackpacksRepository,
    private val schedulersProvider: SchedulersProvider = SchedulersProviderImpl()
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<BackpackItemsUIModel>()
    val uiModel: LiveData<BackpackItemsUIModel> = _uiModel
    private var currentJob: Disposable? = null

    fun fetchAndRefreshItemsFor(backpack: Backpack, type: BackpackItemType) {
        _uiModel.value = Loading
        currentJob?.dispose()
        currentJob = repository.getAvailableItemsForCategory(backpack, type)
            .subscribeOn(schedulersProvider.io())
            .map { values -> Success(values) }
            .observeOn(schedulersProvider.main())
            .subscribe(
                { model -> _uiModel.value = model },
                {
                    logE("fetchAndRefreshItemsFor($backpack, $type) = Error: ${it.message}", it, TAG)
                    _uiModel.value = Error(it)
                }
            )
    }

    fun deleteItem(itemId: String) {
        disposables += repository.deleteBackpackItem(itemId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribe(
                { logI("deleteBackpackItem($itemId): Success!") },
                { Log.e(TAG, "deleteBackpackItem($itemId) = Error: $it") }
            )
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.dispose()
    }

    companion object {
        const val TAG: String = "BackpackItemsViewModel"
    }
}
