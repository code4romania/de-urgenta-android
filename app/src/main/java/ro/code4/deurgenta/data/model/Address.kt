package ro.code4.deurgenta.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import org.parceler.Parcel

@Entity(tableName = "address", indices = [Index(value = ["id"], unique = true)])
@Parcel(Parcel.Serialization.FIELD)
class Address {

    @PrimaryKey
    @Expose
    var id: Int = 0

    @Expose
    lateinit var name: String

    @Expose
    lateinit var geolocation: String

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (id != other.id) return false
        if (name != other.name) return false
        if (geolocation != other.geolocation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + geolocation.hashCode()
        return result
    }


}
