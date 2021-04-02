package ro.code4.deurgenta.ui.base
import androidx.lifecycle.ViewModel
import ro.code4.deurgenta.interfaces.ExcludeFromCodeCoverage

interface ViewModelSetter<out T : ViewModel> {
    @get:ExcludeFromCodeCoverage
    val viewModel: T
}