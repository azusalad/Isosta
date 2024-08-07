package io.github.azusalad.isosta.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.azusalad.isosta.model.Thumbnail

@Database(entities = [Thumbnail::class], version = 1, exportSchema = false)
abstract class ThumbnailDatabase : RoomDatabase() {
    abstract fun thumbnailDao(): ThumbnailDao

    companion object {
        @Volatile
        private var Instance: ThumbnailDatabase? = null

        fun getDatabase(context: Context): ThumbnailDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ThumbnailDatabase::class.java, "thumbnail_database")
                    .fallbackToDestructiveMigration()  // old data base is lost upon schema change
                    .build()  // create database instance
                    .also { Instance = it }  // keep a reference to the recently closed database
            }
        }
    }
}
