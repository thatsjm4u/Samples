package `in`.teardown.soft.roomdatabase.model

import android.arch.persistence.room.*

@Entity
data class Task{
    @PrimaryKey val localId: Int
    @ColumnInfo(name = "title") val title: String?
    @ColumnInfo(name = "description") val description: String?
}

@Dao
interface TaskDao{
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE localId IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Task

    @Insert
    fun insertAll(vararg taks: Task)

    @Delete
    fun delete(task: Task)
}