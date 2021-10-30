package ro.code4.deurgenta.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationTypes")
data class LocationType(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: Int,
    @ColumnInfo(name = COLUMN_LABEL) val label: String,
) {
    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_LABEL = "name"
    }

    override fun toString(): String = label
}
