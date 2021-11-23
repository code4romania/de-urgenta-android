package ro.code4.deurgenta.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Entity(tableName = "groups")
@Parcelize
data class Group(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) @Expose val id: String,
    @ColumnInfo(name = COLUMN_NAME) @Expose val name: String,
    @ColumnInfo(name = COLUMN_NUMBER_OF_MEMBERS) @Expose val numberOfMembers: Int,
    @ColumnInfo(name = COLUMN_MAX_NUMBER_OF_MEMBERS) @Expose val maxNumberOfMembers: Int,
    @ColumnInfo(name = COLUMN_IS_USER_ADMIN) @Expose val isCurrentUserAdmin: Boolean
) : Parcelable {
    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_NUMBER_OF_MEMBERS = "numberOfMembers"
        const val COLUMN_MAX_NUMBER_OF_MEMBERS = "maxNumberOfMembers"
        const val COLUMN_IS_USER_ADMIN = "isUserAdmin"
    }
}

val Group.countDisplay: String
    get() = "($numberOfMembers/$maxNumberOfMembers)"
