package ro.code4.deurgenta.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Observable
import ro.code4.deurgenta.data.model.LocationType

@Dao
interface UserDao {

    @Query("SELECT * FROM locationTypes")
    fun getLocationTypes(): Observable<List<LocationType>>

    @Query("DELETE FROM locationTypes")
    fun clearLocationTypes()

    @Insert(onConflict = REPLACE)
    fun saveLocationTypes(locationTypes: List<LocationType>)

    @Transaction
    fun updateLocationTypes(newLocations: List<LocationType>) {
        clearLocationTypes()
        saveLocationTypes(newLocations)
    }
}
