package com.valdays.journal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valdays.journal.data.local.entity.MediaEntity
import com.valdays.journal.data.local.entity.NoteEntity
import com.valdays.journal.data.local.entity.ReminderEntity
import com.valdays.journal.data.local.dao.NoteDao

@Database(
    entities = [NoteEntity::class, MediaEntity::class, ReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
