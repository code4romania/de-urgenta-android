package ro.code4.deurgenta.data.model

import androidx.room.Entity

/**
 * Needed because of a many to many relationship, a group can reference multiple people and a person can be in
 * several groups.
 */
@Entity(
    tableName = "groupsMemberRefs",
    primaryKeys = ["groupId", "memberId"]
)
data class GroupsAndMembersRefs(
    val groupId: String,
    val memberId: String
)
