package ro.code4.deurgenta.services

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.User
import ro.code4.deurgenta.data.model.response.LoginResponse
import ro.code4.deurgenta.data.model.response.RegisterResponse

interface ApiInterface {
    @POST("auth/register")
    fun register(@Body data: Register): Observable<RegisterResponse>

    @POST("auth/login")
    fun login(@Body user: User): Observable<LoginResponse>
}
