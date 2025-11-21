package com.example.todolist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    long insert(TodoEntity entity);

    @Update
    void update(TodoEntity entity);

    @Delete
    void delete(TodoEntity entity);

    @Query("SELECT * FROM todos")
    LiveData<List<TodoEntity>> getAll();

    @Query("SELECT * FROM todos WHERE id = :id LIMIT 1")
    LiveData<TodoEntity> getById(long id);

    @Query("SELECT * FROM todos WHERE reminder_time IS NOT NULL AND reminder_time > :now AND completed = 0")
    List<TodoEntity> getPendingReminders(long now);
}

