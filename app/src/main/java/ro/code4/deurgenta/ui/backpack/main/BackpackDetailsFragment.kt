package ro.code4.deurgenta.ui.backpack.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_backpack_details.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.parceler.Parcels
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.ui.backpack.items.BackpackItemsFragment
import ro.code4.deurgenta.ui.base.ViewModelFragment

class BackpackDetailsFragment : ViewModelFragment<BackpackDetailsViewModel>() {
    override val screenName: Int
        get() = R.string.app_name
    override val layout: Int
        get() = R.layout.fragment_backpacks
    override val viewModel: BackpackDetailsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_backpack_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backpack: Backpack = Parcels.unwrap(arguments?.getParcelable(KEY_BACKPACK))
        requireActivity().title = backpack.name

        buildBackpackUI(backpack)

        choose_person_responsible.setOnClickListener {
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
                backpack_content, false
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
                            BackpackItemsFragment.KEY_BACKPACK to Parcels.wrap(backpack),
                            BackpackItemsFragment.KEY_ITEM_TYPE to Parcels.wrap(type)
                        )
                    )
                }
            }
            backpack_content.addView(entryView)
        }
        typedIcons.recycle()
    }

    companion object {
        const val KEY_BACKPACK = "key_backpack"
    }
}


