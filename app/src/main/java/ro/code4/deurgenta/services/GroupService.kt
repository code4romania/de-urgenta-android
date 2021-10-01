package ro.code4.deurgenta.services

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.requests.CreateGroupRequest

interface GroupService {
    @POST("group")
    fun createGroup(@Body request: CreateGroupRequest): Single<Group>
}
