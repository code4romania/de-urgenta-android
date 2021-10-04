package ro.code4.deurgenta.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ro.code4.deurgenta.data.model.Group

@Dao
interface GroupDao {
    @Query("SELECT * FROM `groups`")
    fun list(): Single<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg groups: Group): Completable

    @Query("DELETE FROM `groups` WHERE `id` = :id")
    fun deleteById(id: String): Completable
}
