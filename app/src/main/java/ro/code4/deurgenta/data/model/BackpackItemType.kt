package ro.code4.deurgenta.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Possible categories:
 *
 * 1 - apă și alimente
 * 2 - articole de igienă
 * 3 - trusă de prim ajutor
 * 4 - documente
 * 5 - articole de supraviețuire
 * 6 - diverse
 */
@Parcelize
enum class BackpackItemType(val id: Int) : Parcelable {

    Food(1), Hygiene(2), FirstAid(3), Documents(4), Survival(5), Misc(6);

    companion object {
        fun from(id: Int): BackpackItemType = values().firstOrNull { it.id == id } ?: error(
            "Unknown BackpackItemType requested: $id"
        )
    }
}