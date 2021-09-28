package ro.code4.deurgenta.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.code4.deurgenta.helper.Resource
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel

class CoursesFilterViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _filterOptions = MutableLiveData<Resource<Pair<List<String>, List<String>>>>()
    val filterOptions: LiveData<Resource<Pair<List<String>, List<String>>>> = _filterOptions

    init {
        fetchFilterOptions()
    }

    private fun fetchFilterOptions() {
        _filterOptions.value = Resource.loading()
        disposables.add(
            repository.fetchFilterOptions()
                .subscribeOn(Schedulers.io())
                .map { filterData ->
                    val types = mutableSetOf<String>()
                    val cities = mutableSetOf<String>()
                    filterData.forEach {
                        types.add(it.type)
                        cities.add(extractCity(it.location))
                    }
                    // to test empty list UI also added one extra city that will always be empty, Iași
                    cities.add("Iași")
                    Pair(types.toList(), cities.toList())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _filterOptions.value = Resource.success(it)
                }, {
                    _filterOptions.value = Resource.error(it)
                })
        )
    }

    private fun extractCity(fullLocation: String): String = fullLocation.substring(fullLocation.lastIndexOf(" "))
}