package ro.code4.deurgenta.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Observable
import ro.code4.deurgenta.data.model.MapAddress

@Dao
interface AddressDao {
    @Query("SELECT * FROM mapaddress")
    fun getAll(): Observable<List<MapAddress>>

    @Insert(onConflict = REPLACE)
    fun save(vararg address: MapAddress)
}