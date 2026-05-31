package com.valdays.journal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val noteId: Long?,
    val uriString: String,
    val mimeType: String,
    val addedAt: Long
)
