package ro.code4.deurgenta.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback

abstract class ViewModelFragment<out T : BaseViewModel>() : BaseAnalyticsFragment(), Layout,
    ViewModelSetter<T> {
    lateinit var mContext: Context
    lateinit var mActivity: BaseActivity<*>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = activity as BaseActivity<*>
    }

    protected fun onBackPressedCallback() = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleOnBackPressedInternal()
        }
    }

    open fun handleOnBackPressedInternal() = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    fun showDefaultErrorSnackBar(view: View) {
        mActivity.showDefaultErrorSnackBar(view)
    }
}