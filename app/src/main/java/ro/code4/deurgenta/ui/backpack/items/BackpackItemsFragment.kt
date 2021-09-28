package ro.code4.deurgenta.ui.backpack.items

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.databinding.FragmentBackpackItemsBinding
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.ui.backpack.edit.EditBackpackItemFragment
import ro.code4.deurgenta.ui.backpack.edit.NewItemData
import ro.code4.deurgenta.ui.base.BaseFragment

class BackpackItemsFragment : BaseFragment(R.layout.fragment_backpack_items) {

    override val screenName: Int
        get() = R.string.app_name
    private val viewModel: BackpackItemsViewModel by viewModel()
    private val binding: FragmentBackpackItemsBinding by viewBinding(FragmentBackpackItemsBinding::bind)
    private lateinit var backpack: Backpack
    private lateinit var type: BackpackItemType
    private val itemsAdapter: BackpackItemsAdapter by lazy {
        BackpackItemsAdapter(requireContext(), {
            findNavController().navigate(
                R.id.action_backpackItems_to_editBackpackItem,
                bundleOf(
                    EditBackpackItemFragment.KEY_REQUEST_TYPE to EditBackpackItemFragment.TYPE_EDIT_ITEM,
                    EditBackpackItemFragment.KEY_EDIT_ITEM_DATA to it
                )
            )
        }, { itemId -> viewModel.deleteItem(itemId) })
    }
    private var loadingAnimator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backpack = arguments?.getParcelable(KEY_BACKPACK)
            ?: error("A backpack reference must be provided to show it items!")
        type = arguments?.getParcelable(KEY_ITEM_TYPE)
            ?: error("A reference to the type of backpack items must be provided!")

        binding.changeContents.setOnClickListener {
            findNavController().navigate(
                R.id.action_backpackItems_to_editBackpackItem,
                bundleOf(
                    EditBackpackItemFragment.KEY_REQUEST_TYPE to EditBackpackItemFragment.TYPE_NEW_ITEM,
                    EditBackpackItemFragment.KEY_NEW_ITEM_DATA to NewItemData(backpack.id, type)
                )
            )
        }

        with(binding.backpackItems) {
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
        binding.loadingIndicator.visibility = View.VISIBLE
        loadingAnimator?.cancel()
        loadingAnimator = binding.loadingIndicator.setToRotateIndefinitely()
        loadingAnimator?.start()
        binding.empty.visibility = View.GONE
        binding.backpackItems.visibility = View.GONE
    }

    private fun displayEmpty() {
        binding.loadingIndicator.visibility = View.GONE
        loadingAnimator?.cancel()
        binding.empty.visibility = View.VISIBLE
        binding.backpackItems.visibility = View.GONE
    }

    private fun displayItems() {
        binding.loadingIndicator.visibility = View.GONE
        loadingAnimator?.cancel()
        binding.empty.visibility = View.GONE
        binding.backpackItems.visibility = View.VISIBLE
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