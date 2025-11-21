package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.data.TodoEntity;
import com.example.todolist.repository.TodoRepository;

public class AddEditTodoViewModel extends AndroidViewModel {

    private final TodoRepository repository;

    public AddEditTodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
    }

    public LiveData<TodoEntity> getTodo(long id) {
        return repository.getById(id);
    }

    public void insert(TodoEntity entity) {
        repository.insert(entity);
    }

    public long insertSync(TodoEntity entity) {
        return repository.insertSync(entity);
    }

    public void update(TodoEntity entity) {
        repository.update(entity);
    }

    public void delete(TodoEntity entity) {
        repository.delete(entity);
    }
}

