package ro.code4.deurgenta.data.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Entity(
    tableName = BackpackItem.TABLE_NAME, foreignKeys = [
        ForeignKey(
            entity = Backpack::class,
            parentColumns = [Backpack.COLUMN_ID],
            childColumns = [BackpackItem.COLUMN_BACKPACK_ID]
        )
    ]
)
@Parcelize
data class BackpackItem(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
    @ColumnInfo(name = COLUMN_BACKPACK_ID, index = true) val backpackId: String,
    @ColumnInfo(name = COLUMN_NAME) val name: String,
    @ColumnInfo(name = COLUMN_AMOUNT) val amount: Int,
    @ColumnInfo(name = COLUMN_EXPIRATION_DATE) val expirationDate: ZonedDateTime?,
    @ColumnInfo(name = COLUMN_TYPE) val type: BackpackItemType
) : Parcelable {

    companion object {
        const val TABLE_NAME = "backpackItems"
        const val COLUMN_ID = "id"
        const val COLUMN_BACKPACK_ID = "backpackId"
        const val COLUMN_NAME = "name"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_EXPIRATION_DATE = "expiration_date"
        const val COLUMN_TYPE = "type"
    }
}

object BackpackItemConverters {
    @TypeConverter
    @JvmStatic
    fun fromTypeToInt(type: BackpackItemType): Int = type.id

    @TypeConverter
    @JvmStatic
    fun fromIntToType(id: Int): BackpackItemType = BackpackItemType.from(id)

    @TypeConverter
    @JvmStatic
    fun fromDateToString(date: ZonedDateTime?): String? = date?.toString()

    @TypeConverter
    @JvmStatic
    fun fromStringToZonedDateTime(strDate: String?): ZonedDateTime? = strDate?.let { ZonedDateTime.parse(it) }
}