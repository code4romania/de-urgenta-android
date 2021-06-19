package ro.code4.deurgenta.services

import io.reactivex.Single
import retrofit2.http.*
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.CreateNewBackpack

interface BackpackService {
    @GET("backpacks")
    fun getBackpacks(): Single<List<Backpack>>

    @POST("backpack")
    fun saveNewBackpack(@Body data: CreateNewBackpack): Single<Backpack>

    @DELETE("backpack/{backpackId}")
    fun deleteBackpack(@Path("backpackId") backpackId: String)

    @GET("backpack/{backpackId}/items")
    fun getBackpackItems(@Path("backpackId") backpackId: String): Single<List<BackpackItem>>

    @GET("backpack/{backpackId}/{categoryId}/items")
    fun getBackpackItemsForCategory(
        @Path("backpackId") backpackId: String,
        @Path("categoryId") categoryId: String
    ): Single<List<BackpackItem>>
}
