package ro.code4.deurgenta.ui.group.listing

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentGroupsListingBinding
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class GroupsListingFragment : BaseFragment(R.layout.fragment_groups_listing) {

    override val screenName: Int
        get() = R.string.analytics_title_groups_listing
    private val binding: FragmentGroupsListingBinding by viewBinding(FragmentGroupsListingBinding::bind)
    private val viewModel: GroupsListingViewModel by viewModel()
    private val groupsAdapter: GroupsListingAdapter by lazy {
        GroupsListingAdapter(
            context = requireContext(),
            onGroupOpened = viewModel::showMembersFor,
            onGroupClosed = viewModel::hideMembersFor,
            onGroupEdit = {
                Toast.makeText(requireContext(), "Not implemented!", Toast.LENGTH_SHORT).show()
            },
            onGroupExit = { _, _ ->
                Toast.makeText(requireContext(), "Not implemented!", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private var animator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.groupsDataModel.observe(viewLifecycleOwner) { state ->
            when (state) {
                GroupListingState.Loading -> {
                    animator?.cancel()
                    animator = binding.loadingIndicator.setToRotateIndefinitely()
                    animator?.start()
                    binding.loadingIndicator.visibility = View.VISIBLE
                    binding.infoText.visibility = View.GONE
                    binding.uiComponents.visibility = View.GONE
                }
                is GroupListingState.Error -> {
                    animator?.cancel()
                    binding.loadingIndicator.visibility = View.GONE
                    binding.infoText.visibility = View.VISIBLE
                    binding.infoText.text = getString(R.string.group_error_listing, state.exception.message)
                    binding.uiComponents.visibility = View.GONE
                }
                is GroupListingState.Success -> {
                    animator?.cancel()
                    binding.loadingIndicator.visibility = View.GONE
                    binding.infoText.visibility = View.GONE
                    binding.uiComponents.visibility = View.VISIBLE
                    groupsAdapter.submitList(state.data)
                }
            }
        }
        with(binding.groupsList) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupsAdapter
        }
        binding.btnCreateNewGroup.setOnClickListener {
            findNavController().navigate(GroupsListingFragmentDirections.actionGroupsListingToCreateNewGroup())
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.group_title_listing)
    }
}
