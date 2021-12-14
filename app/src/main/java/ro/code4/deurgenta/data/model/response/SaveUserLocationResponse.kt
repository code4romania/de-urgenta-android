package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose

data class SaveUserLocationResponse(
    @Expose val id: String,
    @Expose val address: String,
    @Expose val latitude: Double,
    @Expose val longitude: Double,
    @Expose val type: Int
)
