package io.github.azusalad.isosta.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import io.github.azusalad.isosta.model.IsostaUser
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IsostaUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // If you insert from more than one screen, you need an actual conflict strategy
    suspend fun insert (user: IsostaUser)

    @Update
    suspend fun update(user: IsostaUser)

    @Delete
    suspend fun delete(user: IsostaUser)

    @Query("SELECT * from isostaUser WHERE profileLink = :profileLink")
    fun getIsostaUser(profileLink: String): Flow<IsostaUser>

    @Query("SELECT * from isostaUser ORDER BY date DESC")
    fun getAllIsostaUser(profileLink: String): Flow<IsostaUser>
}

