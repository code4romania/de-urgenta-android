package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterResponse {
    @Expose
    @SerializedName("message")
    lateinit var message: String

    override fun toString(): String = message
}