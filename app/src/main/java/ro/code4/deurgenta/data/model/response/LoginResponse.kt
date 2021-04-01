package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @Expose
    @SerializedName("access_token")
    lateinit var accessToken: String
}