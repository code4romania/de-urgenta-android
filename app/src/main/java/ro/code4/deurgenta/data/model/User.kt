package ro.code4.deurgenta.data.model

import com.google.gson.annotations.Expose

class User(
    @field:Expose var email: String,
    @field:Expose var password: String
)
