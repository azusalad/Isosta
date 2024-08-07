package io.github.azusalad.isosta.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import io.github.azusalad.isosta.model.Thumbnail
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ThumbnailDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // If you insert from more than one screen, you need an actual conflict strategy
    suspend fun insert (user: Thumbnail)

    @Update
    suspend fun update(user: Thumbnail)

    @Delete
    suspend fun delete(user: Thumbnail)

    @Query("SELECT * from thumbnail WHERE postLink = :postLink")
    fun loadThumbnail(postLink: String): Flow<Thumbnail>

    @Query("SELECT * from thumbnail ORDER BY date DESC")
    fun loadAllThumbnail(): Flow<Thumbnail>
}

