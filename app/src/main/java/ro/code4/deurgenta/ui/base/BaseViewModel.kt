package ro.code4.deurgenta.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import ro.code4.deurgenta.helper.SingleLiveEvent

abstract class BaseViewModel : ViewModel(), KoinComponent {
    val messageIdToastLiveData = SingleLiveEvent<String>()
    val disposables = CompositeDisposable()

    fun messageToast(): LiveData<String> = messageIdToastLiveData
    open fun onError(throwable: Throwable) = Unit

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}