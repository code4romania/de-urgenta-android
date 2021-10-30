package ro.code4.deurgenta.services

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ro.code4.deurgenta.data.model.response.LocationTypeResponse

interface UserService {

    @GET("/user/location-types")
    fun fetchLocationTypes(): Single<List<LocationTypeResponse>>
}
