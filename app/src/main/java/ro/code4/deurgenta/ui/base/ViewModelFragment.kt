package ro.code4.deurgenta.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.code4.deurgenta.interfaces.Layout
import ro.code4.deurgenta.interfaces.ViewModelSetter

abstract class ViewModelFragment<out T : BaseViewModel> : BaseAnalyticsFragment(), Layout,
    ViewModelSetter<T> {
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }
}