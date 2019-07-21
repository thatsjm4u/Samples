package `in`.teardown.soft.roomdatabase.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Task::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}