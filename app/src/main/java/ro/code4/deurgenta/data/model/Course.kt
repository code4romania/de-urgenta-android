package ro.code4.deurgenta.data.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import org.threeten.bp.ZonedDateTime

@Entity(tableName = Course.TABLE_NAME)
@TypeConverters(CourseTypeConverters::class)
@Parcelize
data class Course(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
    @ColumnInfo(name = COLUMN_NAME) val name: String,
    @ColumnInfo(name = COLUMN_PROVIDER) val provider: String,
    @ColumnInfo(name = COLUMN_DATE) val date: ZonedDateTime,
    @ColumnInfo(name = COLUMN_LOCATION) val location: String,
    @ColumnInfo(name = COLUMN_ACTION_URL) val url: String,
    @ColumnInfo(name = COLUMN_DESCRIPTION) val description: String,
    @ColumnInfo(name = COLUMN_TYPE) val type: String,
) : Parcelable {

    companion object {
        const val TABLE_NAME = "courses"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PROVIDER = "provider"
        const val COLUMN_DATE = "date"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_ACTION_URL = "url"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TYPE = "type"
    }
}

object CourseTypeConverters {

    @JvmStatic
    @TypeConverter
    fun fromDate(date: ZonedDateTime): String = date.toString()

    @JvmStatic
    @TypeConverter
    fun fromString(dateStr: String): ZonedDateTime = ZonedDateTime.parse(dateStr)
}

data class CourseFilterValues(val type: String, val location: String)