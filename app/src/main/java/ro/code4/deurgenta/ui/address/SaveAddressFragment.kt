package ro.code4.deurgenta.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.MapAddress
import ro.code4.deurgenta.databinding.FragmentSaveAddressBinding
import ro.code4.deurgenta.interfaces.ClickButtonCallback
import ro.code4.deurgenta.ui.base.ViewModelFragment

class SaveAddressFragment : ViewModelFragment<SaveAddressViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_save_address

    override val screenName: Int
        get() = R.string.save_address

    override val viewModel: SaveAddressViewModel by viewModel()

    lateinit var viewBinding: FragmentSaveAddressBinding

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

        arguments?.let { bundle ->
            bundle.getParcelable<MapAddress>("mapAddress")?.let {
                viewBinding.homeAddress = it.streetAddress
            }
        }

        viewBinding.closeSaveAddress.setOnClickListener {
            findNavController().navigate(R.id.back_to_configure_profile)
        }

        viewBinding.homeAddressLayout.textHeader =
            getText(R.string.title_home_address) as String

        viewBinding.homeAddressLayout.drawableRes =
            R.drawable.ic_home

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback()
        )
        viewBinding.addNewAddressCallback = ClickButtonCallback {
            findNavController().navigate(SaveAddressFragmentDirections.actionNavigateSaveAddressToAddressTypeFragment())
        }
    }

    override fun handleOnBackPressedInternal() {
        val directions = SaveAddressFragmentDirections.backToConfigureProfile()
        findNavController().navigate(directions)
    }
}
