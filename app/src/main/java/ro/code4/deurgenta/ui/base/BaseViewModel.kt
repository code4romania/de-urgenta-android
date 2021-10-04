package ro.code4.deurgenta.ui.base

import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.core.component.KoinComponent
import ro.code4.deurgenta.helper.SingleLiveEvent

abstract class BaseViewModel : ViewModel(), KoinComponent, Observable {
    val messageIdToastLiveData = SingleLiveEvent<String>()
    val disposables = CompositeDisposable()

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    fun messageToast(): LiveData<String> = messageIdToastLiveData
    open fun onError(throwable: Throwable) = Unit

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }


    override fun addOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks!!.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.remove(callback)
    }

    protected fun Disposable.connect() = disposables.add(this)
}
