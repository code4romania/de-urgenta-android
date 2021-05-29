package ro.code4.deurgenta.ui.backpack.items

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_backpack_items.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.parceler.Parcels
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.ui.backpack.edit.EditBackpackItemFragment
import ro.code4.deurgenta.ui.backpack.edit.NewItemData
import ro.code4.deurgenta.ui.base.ViewModelFragment

class BackpackItemsFragment : ViewModelFragment<BackpackItemsViewModel>() {
    override val screenName: Int
        get() = R.string.app_name
    override val layout: Int
        get() = R.layout.fragment_backpack_items
    override val viewModel: BackpackItemsViewModel by viewModel()
    private lateinit var backpack: Backpack
    private lateinit var type: BackpackItemType
    private val itemsAdapter: BackpackItemsAdapter by lazy {
        BackpackItemsAdapter(requireContext(), {
            findNavController().navigate(
                R.id.action_backpackItems_to_editBackpackItem,
                bundleOf(
                    EditBackpackItemFragment.KEY_REQUEST_TYPE to EditBackpackItemFragment.TYPE_EDIT_ITEM,
                    EditBackpackItemFragment.KEY_EDIT_ITEM_DATA to Parcels.wrap(it)
                )
            )
        }, { itemId -> viewModel.deleteItem(itemId) })
    }
    private var loadingAnimator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backpack = Parcels.unwrap(arguments?.getParcelable(KEY_BACKPACK))
        type = Parcels.unwrap(arguments?.getParcelable(KEY_ITEM_TYPE))

        change_contents.setOnClickListener {
            findNavController().navigate(
                R.id.action_backpackItems_to_editBackpackItem,
                bundleOf(
                    EditBackpackItemFragment.KEY_REQUEST_TYPE to EditBackpackItemFragment.TYPE_NEW_ITEM,
                    EditBackpackItemFragment.KEY_NEW_ITEM_DATA to Parcels.wrap(NewItemData(backpack.id, type))
                )
            )
        }

        with(backpack_items) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
            adapter = itemsAdapter
        }

        viewModel.fetchItemsForType(backpack, type)
        viewModel.uiModel.observe(viewLifecycleOwner) { model ->
            when (model) {
                Loading -> displayLoading()
                is Success -> {
                    if (model.items.isEmpty()) {
                        displayEmpty()
                    } else {
                        displayItems()
                        itemsAdapter.submitList(model.items)
                    }
                }
                is Error -> TODO()
            }
        }
    }

    private fun displayLoading() {
        loadingIndicator.visibility = View.VISIBLE
        loadingAnimator?.cancel()
        loadingAnimator = loadingIndicator.setToRotateIndefinitely()
        loadingAnimator?.start()
        empty.visibility = View.GONE
        backpack_items.visibility = View.GONE
    }

    private fun displayEmpty() {
        loadingIndicator.visibility = View.GONE
        loadingAnimator?.cancel()
        empty.visibility = View.VISIBLE
        backpack_items.visibility = View.GONE
    }

    private fun displayItems() {
        loadingIndicator.visibility = View.GONE
        loadingAnimator?.cancel()
        empty.visibility = View.GONE
        backpack_items.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title =
            resources.getStringArray(R.array.backpack_item_types)[BackpackItemType.values().indexOf(type)]
    }

    companion object {
        const val KEY_BACKPACK = "key_backpack"
        const val KEY_ITEM_TYPE = "key_item_type"
    }
}