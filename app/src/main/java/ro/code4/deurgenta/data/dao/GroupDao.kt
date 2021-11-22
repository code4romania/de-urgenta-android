package ro.code4.deurgenta.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.data.model.GroupMember
import ro.code4.deurgenta.data.model.GroupWithMembers
import ro.code4.deurgenta.data.model.GroupsAndMembersRefs

@Dao
interface GroupDao {
    @Query("SELECT * FROM `groups`")
    fun list(): Observable<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg groups: Group): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSync(groups: List<Group>)

    @Query("DELETE FROM `groups` WHERE `id` = :id")
    fun deleteById(id: String): Completable

    @Transaction
    fun saveMembersSync(groupId: String, members: List<GroupMember>) {
        saveMembers(members)
        saveMembersRefs(members.map { GroupsAndMembersRefs(groupId, it.id) })
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMembers(groupMembers: List<GroupMember>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMembersRefs(groupMembersRefs: List<GroupsAndMembersRefs>)

    @Transaction
    @Query("SELECT * FROM groups WHERE id=:groupId")
    fun listMembers(groupId: String): Observable<List<GroupWithMembers>>
}
