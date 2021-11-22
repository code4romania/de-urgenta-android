package ro.code4.deurgenta.ui.group.listing

import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.GroupMember

/**
 * Sealed class modeling the different types of rows(data) in [GroupsListingAdapter].
 */
sealed class GroupListingModel

/**
 * Represents the row showing the actual group names, member count and actions possible on that group(like edit or
 * show more info).
 */
data class GroupInfo(val group: Group) : GroupListingModel()

/**
 * Represents the row showing a member of the group.
 */
data class GroupIndividual(val group: Group, val member: GroupMember) : GroupListingModel()

/**
 * Represents the row showing as a placeholder when the user requests more info about the group.
 */
object GroupLoadingMembers : GroupListingModel()

/**
 * Represents the extra row placed in the last position to create a border for the last item as set in the design.
 */
object GroupFakeDivider : GroupListingModel()
