package ro.code4.deurgenta.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GroupWithMembers(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = GroupMember::class,
        associateBy = Junction(GroupsAndMembersRefs::class, parentColumn = "groupId", entityColumn = "memberId")
    ) val members: List<GroupMember>
)
