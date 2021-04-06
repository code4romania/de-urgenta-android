package ro.code4.deurgenta.data.model

import com.google.gson.annotations.Expose

class Register(
    @field:Expose var firstName: String,
    @field:Expose var lastName: String,
    @field:Expose var email: String,
    @field:Expose var password: String
)