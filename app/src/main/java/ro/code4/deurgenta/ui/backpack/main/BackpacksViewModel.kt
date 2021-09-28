package ro.code4.deurgenta.ui.backpack.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.helper.logE
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel

class BackpacksViewModel(private val repository: Repository) : BaseViewModel() {

    private val _uiModel = MutableLiveData<BackpacksUIModel>()
    val uiModel: LiveData<BackpacksUIModel> = _uiModel

    fun fetchBackpacks() {
        _uiModel.postValue(Loading)

        disposables.add(
            repository.getBackpacks()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { backpacks -> _uiModel.postValue(BackpacksFetched(backpacks)) },
                    { error -> _uiModel.postValue(Error(error)) }
            )
        )
    }

    fun saveNewBackpack(name: String) {
        disposables.add(
            repository.saveNewBackpack(name)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { backpack -> _uiModel.postValue(BackpackAdded(backpack)) },
                    { error ->
                        run {
                            logE("Error while saving new backpack: $error")
                            _uiModel.postValue(Error(error))
                        }
                    }
                )
        )
    }

}

sealed class BackpacksUIModel

class BackpacksFetched(val backpacks: List<Backpack>) : BackpacksUIModel()

class BackpackAdded(val backpack: Backpack) : BackpacksUIModel()

class Error(val throwable: Throwable) : BackpacksUIModel()

object Loading : BackpacksUIModel()
