package com.valdays.journal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val noteId: Long,
    val triggerAtMillis: Long,
    val ringtoneUriString: String?,
    val isCompleted: Boolean = false
)
