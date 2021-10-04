package ro.code4.deurgenta.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "groups")
data class Group(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) @Expose val id: String,
    @ColumnInfo(name = COLUMN_NAME) @Expose val name: String,
    @ColumnInfo(name = COLUMN_NUMBER_OF_MEMBERS) @Expose val numberOfMembers: Int,
    @ColumnInfo(name = COLUMN_ADMIN_ID) @Expose val adminId: String,
    @ColumnInfo(name = COLUMN_ADMIN_FIRSTNAME) @Expose val adminFirstName: String?,
    @ColumnInfo(name = COLUMN_ADMIN_LASTNAME) @Expose val adminLastName: String?
) {
    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_NUMBER_OF_MEMBERS = "numberOfMembers"
        const val COLUMN_ADMIN_ID = "adminId"
        const val COLUMN_ADMIN_FIRSTNAME = "adminFirstName"
        const val COLUMN_ADMIN_LASTNAME = "adminLastName"
    }
}
