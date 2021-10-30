package ro.code4.deurgenta.ui.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ro.code4.deurgenta.data.model.LocationType
import ro.code4.deurgenta.helper.Either
import ro.code4.deurgenta.helper.SchedulersProvider
import ro.code4.deurgenta.helper.plusAssign
import ro.code4.deurgenta.repositories.UserRepository
import ro.code4.deurgenta.ui.base.BaseViewModel

class AddressTypeViewModel(
    private val userRepository: UserRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    private val _locationTypes = MutableLiveData<Either<Throwable, List<LocationType>>>()
    val locationTypes: LiveData<Either<Throwable, List<LocationType>>> = _locationTypes

    init {
        disposables += userRepository.refreshAndFetchLocationTypes()
            .subscribeOn(schedulersProvider.io())
            .map { it.sortedBy { locationType -> locationType.id } }
            .observeOn(schedulersProvider.main())
            .subscribe(
                { _locationTypes.value = Either.Right(it) },
                { _locationTypes.value = Either.Left(it) }
            )
    }

    fun refreshLocationTypes() {
        disposables += userRepository.refreshLocationTypes()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribe(
                { /* ignored as we updated the database which will refresh the UI */ },
                { _locationTypes.value = Either.Left(it) }
            )
    }
}
