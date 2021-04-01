package ro.code4.deurgenta.interfaces

import androidx.lifecycle.ViewModel

interface ViewModelSetter<out T : ViewModel> {
    @get:ExcludeFromCodeCoverage
    val viewModel: T
}