package ro.code4.deurgenta.ui.base

import androidx.annotation.LayoutRes
import ro.code4.deurgenta.interfaces.ExcludeFromCodeCoverage

interface Layout {
    @get:ExcludeFromCodeCoverage
    @get:LayoutRes
    val layout: Int
}
