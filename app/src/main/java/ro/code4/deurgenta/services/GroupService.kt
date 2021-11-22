package ro.code4.deurgenta.services

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.requests.CreateGroupRequest
import ro.code4.deurgenta.data.model.response.GetGroupMembersResponse
import ro.code4.deurgenta.data.model.response.GetMyGroupsResponse

interface GroupService {

    @POST("group")
    fun createGroup(@Body request: CreateGroupRequest): Single<Group>

    @GET("groups/my")
    fun getMyGroups(): Single<List<GetMyGroupsResponse>>

    @GET("group/{groupId}/members")
    fun getMembersOfGroup(@Path("groupId") groupId: String): Single<List<GetGroupMembersResponse>>
}
