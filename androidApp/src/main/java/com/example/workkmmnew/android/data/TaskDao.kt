package com.example.workkmmnew.android.data

import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task) // This will handle updating isDone

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Int): Task?

    // Optionally add method to get only completed tasks
    @Query("SELECT * FROM tasks WHERE isDone = 1")
    suspend fun getCompletedTasks(): List<Task>

    // Optionally add method to get only pending tasks
    @Query("SELECT * FROM tasks WHERE isDone = 0")
    suspend fun getPendingTasks(): List<Task>
}
