package io.github.azusalad.isosta.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.azusalad.isosta.model.IsostaUser

@Database(entities = [IsostaUser::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: UserDatabase? = null

        fun loadDatabase(context: Context): UserDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, UserDatabase::class.java, "user_database")
                    .fallbackToDestructiveMigration()  // old data base is lost upon schema change
                    .build()  // create database instance
                    .also { Instance = it }  // keep a reference to the recently closed database
            }
        }
    }
}
