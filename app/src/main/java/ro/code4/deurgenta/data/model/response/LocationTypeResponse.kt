package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose
import ro.code4.deurgenta.data.model.LocationType

data class LocationTypeResponse(
    @Expose val id: Int,
    @Expose val label: String
)

fun LocationTypeResponse.toModel(): LocationType = LocationType(this.id, this.label)
