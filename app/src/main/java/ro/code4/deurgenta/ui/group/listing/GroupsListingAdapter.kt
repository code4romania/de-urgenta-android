package ro.code4.deurgenta.ui.group.listing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.GroupMember
import ro.code4.deurgenta.databinding.ItemGroupFakeDividerBinding
import ro.code4.deurgenta.databinding.ItemGroupInfoBinding
import ro.code4.deurgenta.databinding.ItemGroupLoadMembersBinding
import ro.code4.deurgenta.databinding.ItemGroupMemberBinding

class GroupsListingAdapter(
    context: Context,
    private val onGroupOpened: (Group) -> Unit,
    private val onGroupClosed: (Group) -> Unit,
    private val onGroupEdit: (Group) -> Unit,
    private val onGroupExit: (Group, GroupMember) -> Unit
) : ListAdapter<GroupListingModel, GroupListingViewHolder<GroupListingModel, *>>(groupModelDiff) {

    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupInfo -> ITEM_GROUP
            is GroupIndividual -> ITEM_MEMBER
            GroupLoadingMembers -> ITEM_LOAD_MEMBERS
            GroupFakeDivider -> ITEM_FAKE_DIVIDER
        }
    }

    @Suppress("UNCHECKED_CAST") // we have a closed hierarchy of types so this cast is safe
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListingViewHolder<GroupListingModel, *> {
        return when (viewType) {
            ITEM_GROUP -> GroupInfoViewHolder(
                ItemGroupInfoBinding.inflate(inflater, parent, false),
                onGroupOpened,
                onGroupClosed,
                onGroupEdit
            )
            ITEM_MEMBER -> GroupMemberViewHolder(
                ItemGroupMemberBinding.inflate(inflater, parent, false),
                onGroupExit
            )
            ITEM_LOAD_MEMBERS -> GroupLoadMembersViewHolder(
                ItemGroupLoadMembersBinding.inflate(inflater, parent, false),
            )
            ITEM_FAKE_DIVIDER -> GroupFakeDividerViewHolder(
                ItemGroupFakeDividerBinding.inflate(inflater, parent, false),
            )
            else -> error("Unknown row type requested for group content listing: $viewType!")
        } as GroupListingViewHolder<GroupListingModel, *>
    }

    override fun onBindViewHolder(holder: GroupListingViewHolder<GroupListingModel, *>, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }

    override fun submitList(list: List<GroupListingModel>?) {
        val copyList = list?.toMutableList()
        val lastItem = list?.lastOrNull()
        if (lastItem != null) {
            val fakeDivider = lastItem as? GroupFakeDivider
            if (fakeDivider == null) {
                copyList?.add(GroupFakeDivider)
            }
        }
        super.submitList(copyList)
    }

    companion object {
        const val ITEM_GROUP = 1
        const val ITEM_MEMBER = 2
        const val ITEM_ADD_MEMBER = 3
        const val ITEM_LOAD_MEMBERS = 4
        const val ITEM_FAKE_DIVIDER = 5
    }
}

private val groupModelDiff = object : DiffUtil.ItemCallback<GroupListingModel>() {

    override fun areItemsTheSame(oldItem: GroupListingModel, newItem: GroupListingModel): Boolean {
        return when (oldItem) {
            is GroupInfo -> newItem is GroupInfo && oldItem.group.id == newItem.group.id
            is GroupIndividual -> newItem is GroupIndividual && oldItem.member == newItem.member
            GroupLoadingMembers -> newItem is GroupLoadingMembers
            GroupFakeDivider -> true
        }
    }

    override fun areContentsTheSame(oldItem: GroupListingModel, newItem: GroupListingModel): Boolean {
        return when (oldItem) {
            is GroupInfo -> newItem is GroupInfo && oldItem.group == newItem.group
            is GroupIndividual -> newItem is GroupIndividual && oldItem.member == newItem.member
            GroupLoadingMembers -> newItem is GroupLoadingMembers
            GroupFakeDivider -> true
        }
    }
}
