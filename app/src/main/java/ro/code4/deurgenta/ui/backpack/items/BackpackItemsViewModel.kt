package ro.code4.deurgenta.ui.backpack.items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel

class BackpackItemsViewModel(private val repository: Repository) : BaseViewModel() {

    private val _uiModel = MutableLiveData<BackpackItemsUIModel>()
    val uiModel: LiveData<BackpackItemsUIModel> = _uiModel

    fun fetchItemsForType(backpack: Backpack, type: BackpackItemType) {
        _uiModel.value = Loading
        disposables.add(
            repository.getItemForBackpackType(backpack, type)
                .subscribeOn(Schedulers.io())
                .map { values -> Success(values) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ model ->
                    _uiModel.value = model
                }, {
                    Log.e(TAG, "fetchItemsForType($backpack, $type) = Error: $it")
                    _uiModel.value = Error(it)
                })
        )
    }

    fun deleteItem(itemId: String) {
        repository.deleteBackpackItem(itemId)
    }

    companion object {
        const val TAG: String = "BackpackItemsViewModel"
    }
}