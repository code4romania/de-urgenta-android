package ro.code4.deurgenta.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groupMembers")
data class GroupMember(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
    @ColumnInfo(name = COLUMN_FIRST_NAME) val firstName: String,
    @ColumnInfo(name = COLUMN_LAST_NAME) val lastName: String,
    @ColumnInfo(name = COLUMN_IS_GROUP_ADMIN) val isGroupAdmin: Boolean,
    @ColumnInfo(name = COLUMN_HAS_VALID_CERTIFICATION) val hasValidCertification: Boolean
) {
    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "firstName"
        const val COLUMN_LAST_NAME = "lastName"
        const val COLUMN_IS_GROUP_ADMIN = "isGroupAdmin"
        const val COLUMN_HAS_VALID_CERTIFICATION = "hasValidCertification"
    }
}

val GroupMember.fullName: String
    get() = "$firstName $lastName"
