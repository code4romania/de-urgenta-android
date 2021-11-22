package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.GroupMember

data class GetMyGroupsResponse(
    @Expose val id: String,
    @Expose val name: String,
    @Expose val numberOfMembers: Int,
    @Expose val maxNumberOfMembers: Int,
    @Expose val isCurrentUserAdmin: Boolean
)

fun GetMyGroupsResponse.toModelGroup(): Group = Group(
    id = this.id,
    name = this.name,
    numberOfMembers = this.numberOfMembers,
    maxNumberOfMembers = this.maxNumberOfMembers,
    isCurrentUserAdmin = this.isCurrentUserAdmin
)

data class GetGroupMembersResponse(
    @Expose val id: String,
    @Expose val firstName: String,
    @Expose val lastName: String,
    @Expose val isGroupAdmin: Boolean,
    @Expose val hasValidCertification: Boolean
)

fun GetGroupMembersResponse.toModelGroupMember(): GroupMember = GroupMember(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    isGroupAdmin = this.isGroupAdmin,
    hasValidCertification = this.hasValidCertification
)
