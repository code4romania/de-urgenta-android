package ro.code4.deurgenta.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = Backpack.TABLE_NAME)
@Parcelize
data class Backpack(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
    @ColumnInfo(name = COLUMN_NAME) val name: String?,
    @ColumnInfo(name = COLUMN_CONTRIBUTORS_COUNT) val numberOfContributors: Int,
    @ColumnInfo(name = COLUMN_CONTRIBUTORS_MAX) val maxNumberOfContributors: Int
) : Parcelable {

    companion object {
        const val TABLE_NAME = "backpacks"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CONTRIBUTORS_COUNT = "numberOfContributors"
        const val COLUMN_CONTRIBUTORS_MAX = "maxNumberOfContributors"

        val EMPTY = Backpack("EMPTY_ID", null, 0, 0)
    }
}
