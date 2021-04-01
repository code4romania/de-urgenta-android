package ro.code4.deurgenta.interfaces

import androidx.annotation.LayoutRes

interface Layout {
    @get:ExcludeFromCodeCoverage
    @get:LayoutRes
    val layout: Int
}
