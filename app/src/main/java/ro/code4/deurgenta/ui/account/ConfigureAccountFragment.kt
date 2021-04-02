package ro.code4.deurgenta.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.ViewModelFragment
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.databinding.OnboardingConfigureAccountBinding
import ro.code4.deurgenta.interfaces.ConfigureCallback

class ConfigureAccountFragment : ViewModelFragment<ConfigureAccountViewModel>() {

    override val layout: Int
        get() = R.layout.onboarding_configure_account

    override val screenName: Int
        get() = R.string.create_your_account

    override val viewModel: ConfigureAccountViewModel by viewModel()

    lateinit var viewBinding: OnboardingConfigureAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DataBindingUtil.inflate(inflater, layout, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = viewLifecycleOwner
        configureCallbacks()
    }

    @SuppressLint("LongLogTag")
    private fun configureCallbacks() {
        viewBinding.callbackAddresses = object : ConfigureCallback {
            override fun configure() {
                Log.d(TAG, "configure addresses")
                val bundle = bundleOf("configure" to "configure_address")
                findNavController().navigate(R.id.navigate_to_configure_address, bundle)
            }
        }
    }

    companion object {
        const val TAG: String = "ConfigureAccountFragment"
    }
}