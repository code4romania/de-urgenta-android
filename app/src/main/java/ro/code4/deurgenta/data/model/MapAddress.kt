package ro.code4.deurgenta.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = MapAddress.TABLE_NAME
)
@Parcelize
data class MapAddress @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,

    @ColumnInfo(name = COLUMN_LATITUDE)
    val latitude: Double,

    @ColumnInfo(name = COLUMN_LONGITUDE)
    val longitude: Double,

    @ColumnInfo(name = COLUMN_ADDRESS)
    val fullAddress: String,

    @ColumnInfo(name = COLUMN_STREET_ADDRESS)
    var streetAddress: String? = null,

    @ColumnInfo(name = COLUMN_TYPE)
    var type: MapAddressType = MapAddressType.HOME
) : Parcelable {

    override fun toString(): String = fullAddress

    companion object {
        const val TABLE_NAME = "mapAddress"
        const val COLUMN_ID = "id"
        const val COLUMN_TYPE = "address_type"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_STREET_ADDRESS = "street_address"
    }

    object MapAddressConverters {
        @TypeConverter
        @JvmStatic
        fun fromTypeToInt(type: MapAddressType): Int = type.id

        @TypeConverter
        @JvmStatic
        fun fromIntToType(id: Int): MapAddressType = MapAddressType.from(id)
    }
}
