package io.github.azusalad.isosta.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.azusalad.isosta.model.IsostaUser
import io.github.azusalad.isosta.model.Thumbnail
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // If existing item, ignore the new item
    suspend fun insert (user: IsostaUser)

    @Update
    suspend fun update(user: IsostaUser)

    @Delete
    suspend fun delete(user: IsostaUser)

    @Query("SELECT * from user WHERE profileHandle = :userHandle")
    fun loadUser(userHandle: String): Flow<IsostaUser>

    @Query("SELECT * from user ORDER BY profileHandle ASC")
    fun loadAllUser(): Flow<List<IsostaUser>>
}
