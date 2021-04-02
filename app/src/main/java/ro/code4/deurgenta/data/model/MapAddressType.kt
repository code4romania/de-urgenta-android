package ro.code4.deurgenta.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Possible address types:
 *
 * 1 - Home address
 * 2 - Work address
 * 3 - School address
 * 4 - Gym address
 * 5 - Other address
 */
@Parcelize
enum class MapAddressType(val id: Int) : Parcelable {

    HOME(1), WORK(2), SCHOOL(3), GYM(4), OTHER(5);

    companion object {
        fun from(id: Int): MapAddressType = values().firstOrNull { it.id == id } ?: error(
            "Unknown address requested: $id"
        )
    }
}