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
    @Insert(onConflict = OnConflictStrategy.IGNORE)  // If existing item, ignore the new item
    suspend fun insert(thumbnail: Thumbnail)

    @Update
    suspend fun update(thumbnail: Thumbnail)

    @Delete
    suspend fun delete(thumbnail: Thumbnail)

    @Query("SELECT * from thumbnail WHERE postLink = :postLink")
    fun loadThumbnail(postLink: String): Flow<Thumbnail>

    @Query("SELECT * from thumbnail ORDER BY date DESC")
    fun loadAllThumbnail(): Flow<List<Thumbnail>>
}

