package ro.code4.deurgenta.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.MapAddressType
import ro.code4.deurgenta.databinding.FragmentConfigureAccountBinding
import ro.code4.deurgenta.interfaces.ClickButtonCallback
import ro.code4.deurgenta.ui.base.ViewModelFragment

class ConfigureAccountFragment : ViewModelFragment<ConfigureAccountViewModel>() {

    override val layout: Int
        get() = R.layout.fragment_configure_account

    override val screenName: Int
        get() = R.string.create_your_account

    override val viewModel: ConfigureAccountViewModel by viewModel()

    lateinit var viewBinding: FragmentConfigureAccountBinding

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
        viewBinding.callbackAddresses = ClickButtonCallback {
            val directions = ConfigureAccountFragmentDirections.actionConfigureAddress(mapAddressType = MapAddressType.HOME, R.string.add_home_address)
            findNavController().navigate(directions)
        }

        viewBinding.callbackGroup = ClickButtonCallback {
            findNavController().navigate(R.id.action_configure_group)
        }

        viewBinding.callbackBackpack = ClickButtonCallback {
            findNavController().navigate(R.id.action_configure_backpack)
        }
    }
}
