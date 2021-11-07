package ro.code4.deurgenta.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType

@Dao
interface BackpackDao {

    @Query("SELECT * FROM backpacks")
    fun getAllBackpacks(): Single<List<Backpack>>

    @Insert(onConflict = REPLACE)
    fun saveBackpack(backpacks: List<Backpack>)

    @Query("SELECT * FROM backpackItems WHERE backpackId=:backpackId AND type=:type")
    fun getAvailableItemsForCategory(backpackId: String, type: BackpackItemType): Observable<List<BackpackItem>>

    @Query("SELECT * FROM backpackItems WHERE backpackId=:backpackId AND type=:type")
    fun getAvailableItemsForCategorySync(backpackId: String, type: BackpackItemType): List<BackpackItem>

    @Transaction
    fun updateContainedItems(toBeInserted: List<BackpackItem>, toBeDeleted: List<BackpackItem>) {
        if (toBeDeleted.isNotEmpty()) {
            deleteFromContainedItems(toBeDeleted)
        }
        if (toBeInserted.isNotEmpty()) {
            addToContainedItems(toBeInserted)
        }
    }

    @Insert(onConflict = REPLACE)
    fun addToContainedItems(items: List<BackpackItem>)

    @Delete
    fun deleteFromContainedItems(items: List<BackpackItem>)

    @Query("DELETE FROM backpackItems WHERE id=:itemId")
    fun deleteContainedItem(itemId: String)

    @Insert(onConflict = REPLACE)
    fun saveBackpackItem(vararg backpackItem: BackpackItem)

    @Update(onConflict = REPLACE)
    fun updateBackpackItem(vararg backpackItem: BackpackItem)
}
