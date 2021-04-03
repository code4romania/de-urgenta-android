package ro.code4.deurgenta.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import org.koin.core.inject
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.response.RegisterResponse
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel
import ro.code4.deurgenta.ui.login.LoginActivity

class RegisterViewModel : BaseViewModel() {

    private val repository: Repository by inject()

    private val registerLiveData = SingleLiveEvent<Result<Class<*>>>()

    fun registered(): LiveData<Result<Class<*>>> = registerLiveData

    fun register(data: Register): Observable<RegisterResponse> {

        Log.d("TEST", "SEND ${data.firstName} .to users.")
        return repository.register(data)
    }

    fun onRegisterSuccess() {
        // TODO: change login activity with register success fragment
        val nextActivity = LoginActivity::class.java
        registerLiveData.postValue(Result.Success(nextActivity))
    }

    fun onRegisterFail(error: Throwable, message: String = "") {
        registerLiveData.postValue(Result.Failure(error, message))
    }

}