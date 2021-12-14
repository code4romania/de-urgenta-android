package ro.code4.deurgenta.data.model.requests

import com.google.gson.annotations.Expose

data class SaveUserLocationRequest(
    @Expose val address: String,
    @Expose val latitude: Double,
    @Expose val longitude: Double,
    @Expose val type: Int
)
