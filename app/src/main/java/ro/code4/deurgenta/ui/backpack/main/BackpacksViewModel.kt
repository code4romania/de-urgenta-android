package ro.code4.deurgenta.ui.backpack.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel
import java.util.*
import java.util.concurrent.TimeUnit

class BackpacksViewModel(private val repository: Repository) : BaseViewModel() {

    private val _uiModel = MutableLiveData<BackpacksUIModel>()
    val uiModel: LiveData<BackpacksUIModel> = _uiModel

    fun fetchBackpacks() {
        _uiModel.value = Loading
        disposables.add(
            repository.getBackpacks().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _uiModel.value = Success(it)
                }, {
                    _uiModel.value = Error(it)
                })
        )
    }

    fun saveNewBackpack(name: String) {
        val id = UUID.randomUUID().toString()
        repository.saveNewBackpack(Backpack(id, name))
    }

}

sealed class BackpacksUIModel

class Success(val backpacks: List<Backpack>) : BackpacksUIModel()

class Error(val throwable: Throwable) : BackpacksUIModel()

object Loading : BackpacksUIModel()