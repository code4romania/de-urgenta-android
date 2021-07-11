package ro.code4.deurgenta.services

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.User
import ro.code4.deurgenta.data.model.requests.ResetPasswordRequest
import ro.code4.deurgenta.data.model.response.LoginResponse

interface AccountService {
    @POST("auth/register")
    fun register(@Body data: Register): Observable<String>

    @POST("auth/login")
    fun login(@Body user: User): Observable<LoginResponse>

    // TODO replace this with the proper url
    @POST("NOT_AVAILABLE")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Completable
}
