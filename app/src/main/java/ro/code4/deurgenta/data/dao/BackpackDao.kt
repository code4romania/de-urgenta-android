package ro.code4.deurgenta.data.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Single
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType

@Dao
interface BackpackDao {

    @Query("SELECT * FROM backpacks")
    fun getAllBackpacks(): Single<List<Backpack>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBackpack(vararg backpack: Backpack)

    @Query("SELECT * FROM backpackItems WHERE backpackId=:backpackId AND type=:type")
    fun getItemsForType(backpackId: String, type: BackpackItemType): Single<List<BackpackItem>>

    @Query("DELETE FROM backpackItems WHERE id=:itemId")
    fun deleteItem(itemId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBackpackItem(vararg backpackItem: BackpackItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateBackpackItem(vararg backpackItem: BackpackItem)
}
