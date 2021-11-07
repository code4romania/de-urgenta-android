package ro.code4.deurgenta.services

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ro.code4.deurgenta.data.model.requests.CreateBackpackItemRequest
import ro.code4.deurgenta.data.model.requests.CreateNewBackpackRequests
import ro.code4.deurgenta.data.model.requests.UpdateBackpackItem
import ro.code4.deurgenta.data.model.requests.UpdateBackpackItemResponse
import ro.code4.deurgenta.data.model.response.BackpackItemResponse
import ro.code4.deurgenta.data.model.response.BackpackResponse
import ro.code4.deurgenta.data.model.response.CreateNewBackpackResponse
import ro.code4.deurgenta.data.model.response.SaveBackpackItemResponse

interface BackpackService {

    @GET("backpacks")
    fun getBackpacks(): Single<List<BackpackResponse>>

    @POST("backpack")
    fun saveNewBackpack(@Body data: CreateNewBackpackRequests): Single<CreateNewBackpackResponse>

    @DELETE("backpack/{backpackId}")
    fun deleteBackpack(@Path("backpackId") backpackId: String)

    @GET("backpack/{backpackId}/{categoryId}/items")
    fun getAvailableItemsForCategory(
        @Path("backpackId") backpackId: String,
        @Path("categoryId") categoryId: Int
    ): Single<List<BackpackItemResponse>>

    @POST("/backpack/{backpackId}")
    fun addNewItemToContent(
        @Path("backpackId") backpackId: String,
        @Body request: CreateBackpackItemRequest
    ): Single<SaveBackpackItemResponse>

    @PUT("/backpack/item/{itemId}")
    fun updateContainedItem(
        @Path("itemId") itemId: String,
        @Body request: UpdateBackpackItem
    ): Single<UpdateBackpackItemResponse>

    @DELETE("/backpack/item/{itemId}")
    fun removeItemFromContent(@Path("itemId") itemId: String): Completable
}
