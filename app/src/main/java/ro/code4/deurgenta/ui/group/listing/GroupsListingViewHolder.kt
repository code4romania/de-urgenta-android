package ro.code4.deurgenta.ui.group.listing

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.shape.CornerFamily
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.GroupMember
import ro.code4.deurgenta.data.model.countDisplay
import ro.code4.deurgenta.data.model.fullName
import ro.code4.deurgenta.databinding.ItemGroupFakeDividerBinding
import ro.code4.deurgenta.databinding.ItemGroupInfoBinding
import ro.code4.deurgenta.databinding.ItemGroupLoadMembersBinding
import ro.code4.deurgenta.databinding.ItemGroupMemberBinding

sealed class GroupListingViewHolder<M : GroupListingModel, T : ViewBinding>(
    binding: T
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(model: M)
}

class GroupInfoViewHolder(
    private val binding: ItemGroupInfoBinding,
    onGroupOpened: (Group) -> Unit,
    onGroupClosed: (Group) -> Unit,
    onEditGroup: (Group) -> Unit
) : GroupListingViewHolder<GroupInfo, ItemGroupInfoBinding>(binding) {

    private var groupReference: Group? = null
    private val indicatorDown by lazy { ContextCompat.getDrawable(binding.root.context, R.drawable.ic_dropdown) }
    private val indicatorUp by lazy { ContextCompat.getDrawable(binding.root.context, R.drawable.ic_dropup) }

    init {
        binding.groupMoreIndicator.tag = false // true if this row is currently expanded
        binding.groupMoreIndicator.setOnClickListener { view ->
            groupReference?.let {
                val isExpanded = view.tag as Boolean
                binding.groupMoreIndicator.setImageDrawable(if (isExpanded) indicatorDown else indicatorUp)
                view.tag = if (isExpanded) {
                    onGroupClosed(it)
                    false
                } else {
                    onGroupOpened(it)
                    true
                }
            }
        }
        binding.btnEdit.setOnClickListener {
            groupReference?.let { onEditGroup(it) }
        }
    }

    override fun bind(model: GroupInfo) {
        binding.divider.visibility = if (adapterPosition == 0) View.INVISIBLE else View.VISIBLE
        with(model) {
            groupReference = group
            binding.groupName.text = group.name
            binding.groupMemberCount.text = group.countDisplay
            val isExpanded = binding.groupMoreIndicator.tag as Boolean
            binding.groupMoreIndicator.setImageDrawable(if (isExpanded) indicatorUp else indicatorDown)
            binding.btnEdit.visibility = if (group.isCurrentUserAdmin) View.VISIBLE else View.GONE
        }
    }
}

class GroupMemberViewHolder(
    private val binding: ItemGroupMemberBinding,
    onGroupExit: (Group, GroupMember) -> Unit
) : GroupListingViewHolder<GroupIndividual, ItemGroupMemberBinding>(binding) {

    private var groupIndividualReference: GroupIndividual? = null
    private val radius = binding.root.context.resources.getDimension(R.dimen.profile_corner_radius)
    private val badgeSet = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_badge_set)
    private val badgeUnset = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_badge_unset)

    init {
        binding.btnExitGroup.setOnClickListener {
            groupIndividualReference?.let { onGroupExit(it.group, it.member) }
        }
        binding.memberProfile.shapeAppearanceModel = binding.memberProfile.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
    }

    override fun bind(model: GroupIndividual) {
        with(model) {
            groupIndividualReference = model
            binding.memberName.text = member.fullName
            binding.memberProfileOutline.visibility = if (member.isGroupAdmin) View.VISIBLE else View.INVISIBLE
            binding.btnExitGroup.visibility = if (member.isGroupAdmin) View.GONE else View.VISIBLE
            binding.memberProfileBadge.setImageDrawable(if (member.hasValidCertification) badgeSet else badgeUnset)
        }
    }
}

class GroupLoadMembersViewHolder(
    binding: ItemGroupLoadMembersBinding,
) : GroupListingViewHolder<GroupLoadingMembers, ItemGroupLoadMembersBinding>(binding) {

    override fun bind(model: GroupLoadingMembers) {
        // nothing to do
    }
}

class GroupFakeDividerViewHolder(
    binding: ItemGroupFakeDividerBinding,
) : GroupListingViewHolder<GroupFakeDivider, ItemGroupFakeDividerBinding>(binding) {

    override fun bind(model: GroupFakeDivider) {
        // nothing to do
    }
}
