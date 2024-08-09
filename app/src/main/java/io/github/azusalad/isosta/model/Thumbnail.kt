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
    val date: Long = 0,

    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var pictureString: String? = null  // Thumbnail picture as Base64
)
