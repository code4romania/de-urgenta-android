package ro.code4.deurgenta.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ro.code4.deurgenta.data.model.Course
import ro.code4.deurgenta.helper.Resource
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel

class CoursesViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _filteredCourses = MutableLiveData<Resource<List<Course>>>()
    val filteredCourses: LiveData<Resource<List<Course>>> = _filteredCourses

    fun filter(type: String, city: String) {
        _filteredCourses.value = Resource.loading()
        disposables.add(
            repository.fetchCoursesFor(type, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _filteredCourses.value = Resource.success(it)
                }, {
                    _filteredCourses.value = Resource.error(it)
                })
        )
    }
}
