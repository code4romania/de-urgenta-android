package ro.code4.deurgenta.ui.group.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.GroupMember
import ro.code4.deurgenta.data.model.fullName
import ro.code4.deurgenta.databinding.ItemEditGroupMemberBinding

class EditGroupMembersAdapter(
    context: Context,
    private val onRemoveMember: (GroupMember) -> Unit
) : ListAdapter<GroupMember, EditGroupMemberViewHolder>(groupMemberDiff) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditGroupMemberViewHolder {
        return EditGroupMemberViewHolder(ItemEditGroupMemberBinding.inflate(inflater, parent, false), onRemoveMember)
    }

    override fun onBindViewHolder(holder: EditGroupMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val groupMemberDiff = object : DiffUtil.ItemCallback<GroupMember>() {
    override fun areItemsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean {
        return oldItem == newItem
    }
}

class EditGroupMemberViewHolder(
    private val binding: ItemEditGroupMemberBinding,
    private val onRemoveMember: (GroupMember) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val radius = binding.root.context.resources.getDimension(R.dimen.profile_corner_radius)
    private var groupMemberRef: GroupMember? = null

    init {
        binding.memberProfile.shapeAppearanceModel = binding.memberProfile.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
        binding.btnEliminateMember.setOnClickListener {
            groupMemberRef?.let { onRemoveMember(it) }
        }
    }

    fun bind(groupMember: GroupMember) {
        groupMemberRef = groupMember
        binding.memberName.text = groupMember.fullName
        binding.btnEliminateMember.visibility = if (groupMember.isGroupAdmin) View.GONE else View.VISIBLE
        binding.memberProfileOutline.visibility = if (groupMember.isGroupAdmin) View.VISIBLE else View.INVISIBLE
    }
}
