package io.github.azusalad.isosta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thumbnail")
data class Thumbnail (
    @PrimaryKey(autoGenerate = false)
    val postLink: String,
    val picture: String,
    val text: String,
    val sourceHandle: String,
    val date: Int = 0,

    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var pictureString: String? = null
)
