package ro.code4.deurgenta.ui.backpack.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.databinding.FragmentBackpackDetailsBinding
import ro.code4.deurgenta.ui.backpack.items.BackpackItemsFragment
import ro.code4.deurgenta.ui.base.BaseFragment

class BackpackDetailsFragment : BaseFragment(R.layout.fragment_backpack_details) {

    override val screenName: Int
        get() = R.string.app_name
    private val viewModel: BackpackDetailsViewModel by viewModel()
    private val binding: FragmentBackpackDetailsBinding by viewBinding(FragmentBackpackDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backpack: Backpack = arguments?.getParcelable(KEY_BACKPACK)
            ?: error("A backpack reference must be provided to show its details!")
        requireActivity().title = backpack.name

        buildBackpackUI(backpack)

        binding.choosePersonResponsible.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented yet!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildBackpackUI(backpack: Backpack) {
        // TODO fill these arrays with the actual UI design
        val descriptions = resources.getStringArray(R.array.backpack_item_types).asList()
        val typedIcons = resources.obtainTypedArray(R.array.backpack_type_icons)
        BackpackItemType.values().forEachIndexed { index, type ->
            val entryView = requireActivity().layoutInflater.inflate(
                R.layout.item_backpack_item_type,
                binding.backpackContent,
                false
            ) as TextView
            with(entryView) {
                text = descriptions[index]
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), typedIcons.getResourceId(index, -1)),
                    null, null, null
                )
                setOnClickListener {
                    findNavController().navigate(
                        R.id.action_backpackDetails_to_backpackItems, bundleOf(
                            BackpackItemsFragment.KEY_BACKPACK to backpack,
                            BackpackItemsFragment.KEY_ITEM_TYPE to type
                        )
                    )
                }
            }
            binding.backpackContent.addView(entryView)
        }
        typedIcons.recycle()
    }

    companion object {
        const val KEY_BACKPACK = "key_backpack"
    }
}


