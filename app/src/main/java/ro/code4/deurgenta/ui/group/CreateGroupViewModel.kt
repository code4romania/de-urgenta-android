package ro.code4.deurgenta.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.repositories.GroupRepository
import ro.code4.deurgenta.ui.base.BaseViewModel

class CreateGroupViewModel(
    private val groupRepository: GroupRepository
) : BaseViewModel() {
    private val result = MutableLiveData<Result<Group>>()
    val createResult: LiveData<Result<Group>> = result

    fun createGroup(name: String) {
        groupRepository.createGroup(name)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                result.postValue(Result.Loading)
            }
            .subscribe(
                { result.postValue(Result.Success()) },
                { result.postValue(Result.Failure(it)) }
            )
            .connect()
    }
}
