package ro.code4.deurgenta.ui.address

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.Navigation.setViewNavController
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.LocationType
import ro.code4.deurgenta.databinding.FragmentAddressTypeBinding
import ro.code4.deurgenta.helper.Either
import ro.code4.deurgenta.ui.base.BaseFragment

class AddressTypeFragment : BaseFragment(R.layout.fragment_address_type) {
    override val screenName: Int
        get() = R.string.analytics_title_address_type
    private val binding: FragmentAddressTypeBinding by viewBinding(FragmentAddressTypeBinding::bind)
    private val viewModel: AddressTypeViewModel by viewModel()
    private val locationTypeAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<LocationType>()
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewNavController(binding.includedToolbar.toolbar, findNavController())
        binding.locationTypeSelector.apply {
            // 48 dp minimum height in layout,so the drop down sits right at the bottom of the spinner
            dropDownVerticalOffset = resources.getDimension(R.dimen.minimum_touch_size).toInt()
            adapter = locationTypeAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedLocation = parent?.adapter?.getItem(position) as? LocationType
                    binding.btnStartAddressSetup.isEnabled = selectedLocation != null
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.btnStartAddressSetup.isEnabled = false
                }
            }
        }
        binding.btnRetry.setOnClickListener {
            binding.loadingIndicator.visibility = View.VISIBLE
            binding.retryGroup.visibility = View.GONE
            viewModel.refreshLocationTypes()
        }
        binding.btnStartAddressSetup.setOnClickListener {
            val selectedLocationPosition = binding.locationTypeSelector.selectedItemPosition
            if (selectedLocationPosition != AdapterView.INVALID_POSITION) {
                // TODO pass the selected location type and pass it to the next fragment to start the map location
                //  selection
            }
        }
        viewModel.locationTypes.observe(viewLifecycleOwner) { response ->
            binding.loadingIndicator.visibility = View.GONE
            when (response) {
                is Either.Right -> {
                    if (response.data.isNotEmpty()) {
                        setupLocationSelector(response.data)
                        binding.retryGroup.visibility = View.GONE
                    } else {
                        binding.retryGroup.visibility = View.VISIBLE
                    }
                }
                is Either.Left -> binding.retryGroup.visibility = View.VISIBLE
            }
        }
    }

    private fun setupLocationSelector(data: List<LocationType>) {
        locationTypeAdapter.clear()
        locationTypeAdapter.addAll(data)
    }
}
