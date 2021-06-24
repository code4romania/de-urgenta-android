package ro.code4.deurgenta.data.model.requests

import com.google.gson.annotations.Expose

data class ResetPasswordRequest(
    @Expose val email: String,
    @Expose val newPassword: String
)