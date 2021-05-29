package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose

class LoginResponse {
    @Expose
    lateinit var token: String
    @Expose
    var success: Boolean = false
    @Expose
    lateinit var errors: Array<String>
}
