package ro.code4.deurgenta.services

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ro.code4.deurgenta.data.model.requests.SaveUserLocationRequest
import ro.code4.deurgenta.data.model.response.LocationTypeResponse
import ro.code4.deurgenta.data.model.response.SaveUserLocationResponse

interface UserService {

    @GET("/user/location-types")
    fun fetchLocationTypes(): Single<List<LocationTypeResponse>>

    @POST("/user/location")
    fun saveLocation(@Body request: SaveUserLocationRequest): Single<SaveUserLocationResponse>
}
