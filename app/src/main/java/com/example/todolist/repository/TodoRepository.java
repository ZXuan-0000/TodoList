package com.example.todolist.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.todolist.data.AppDatabase;
import com.example.todolist.data.TodoDao;
import com.example.todolist.data.TodoEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TodoRepository {

    private final TodoDao todoDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TodoRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        this.todoDao = database.todoDao();
    }

    public LiveData<List<TodoEntity>> getAll() {
        return todoDao.getAll();
    }

    public LiveData<TodoEntity> getById(long id) {
        return todoDao.getById(id);
    }

    public void insert(TodoEntity entity) {
        executor.execute(() -> todoDao.insert(entity));
    }

    public long insertSync(TodoEntity entity) {
        java.util.concurrent.FutureTask<Long> task = new java.util.concurrent.FutureTask<>(() -> todoDao.insert(entity));
        executor.execute(task);
        try {
            return task.get();
        } catch (Exception e) {
            return -1;
        }
    }

    public void update(TodoEntity entity) {
        executor.execute(() -> todoDao.update(entity));
    }

    public void delete(TodoEntity entity) {
        executor.execute(() -> todoDao.delete(entity));
    }
}

