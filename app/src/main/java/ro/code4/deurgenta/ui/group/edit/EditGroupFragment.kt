package ro.code4.deurgenta.ui.group.edit

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.countDisplay
import ro.code4.deurgenta.databinding.FragmentEditGroupBinding
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class EditGroupFragment : BaseFragment(R.layout.fragment_edit_group) {

    override val screenName: Int
        get() = R.string.analytics_title_groups_edit
    private val binding: FragmentEditGroupBinding by viewBinding(FragmentEditGroupBinding::bind)
    private lateinit var group: Group
    private val viewModel: EditGroupViewModel by viewModel()
    private val groupMembersAdapter by lazy {
        EditGroupMembersAdapter(
            requireContext(),
            onRemoveMember = { targetMember ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.group_edit_remove_member_info)
                    .setPositiveButton(R.string.group_edit_dialog_ok) { _, _ -> viewModel.remove(group, targetMember) }
                    .setNegativeButton(R.string.group_edit_dialog_cancel) { _, _ -> }
                    .show()
            }
        )
    }
    private var animator: ObjectAnimator? = null
    private var groupCount: GroupCount? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        group = arguments?.getParcelable("group") ?: error("A group reference must be supplied in order to edit it!")
        viewModel.fetchGroupMembers(group)
        binding.groupContentList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = groupMembersAdapter
        }
        viewModel.groupMembersState.observe(viewLifecycleOwner) { state ->
            when (state) {
                GroupEditContentState.Loading -> {
                    binding.loadingIndicator.apply {
                        visibility = View.VISIBLE
                        animator?.cancel()
                        animator = setToRotateIndefinitely()
                        animator?.start()
                    }
                    binding.groupContentList.visibility = View.GONE
                    binding.infoText.visibility = View.GONE
                }
                is GroupEditContentState.Error -> {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.infoText.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.group_edit_error_members, state.exception.message)
                    }
                    binding.groupContentList.visibility = View.GONE
                }
                is GroupEditContentState.Success -> {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.infoText.visibility = View.GONE
                    animator?.cancel()
                    binding.groupContentList.visibility = View.VISIBLE
                    groupMembersAdapter.submitList(state.data)
                }
            }
        }
        viewModel.membersCount.observe(viewLifecycleOwner) { memberCount ->
            binding.btnAddMember.visibility = if (memberCount.hasRoomForMembers) View.VISIBLE else View.GONE
            binding.maxMembersInfo.visibility = if (memberCount.hasRoomForMembers) View.GONE else View.VISIBLE
            updateActivityTitle("${group.name}${memberCount?.countDisplay ?: group.countDisplay}")
            groupCount = memberCount
        }
        binding.btnAddMember.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle("${group.name}${groupCount?.countDisplay ?: group.countDisplay}")
    }
}
