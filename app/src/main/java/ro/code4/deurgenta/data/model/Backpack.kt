package ro.code4.deurgenta.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = Backpack.TABLE_NAME)
@Parcelize
data class Backpack(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
    @ColumnInfo(name = COLUMN_NAME) val name: String?,
    // TODO add a creation date to be used in sorting!
) : Parcelable {

    companion object {
        const val TABLE_NAME = "backpacks"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"

        val EMPTY = Backpack("EMPTY_ID", null)
    }
}