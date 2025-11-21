package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.todolist.data.TodoEntity;
import com.example.todolist.repository.TodoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    public enum SortMode {
        DUE_DATE,
        PRIORITY,
        CREATED
    }

    private final TodoRepository repository;
    private final LiveData<List<TodoEntity>> allTodos;
    private final MediatorLiveData<List<TodoEntity>> filteredTodos = new MediatorLiveData<>();
    private String currentCategory = "全部";
    private SortMode currentSort = SortMode.DUE_DATE;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodos = repository.getAll();
        filteredTodos.addSource(allTodos, todos -> filterAndSort(todos, currentCategory, currentSort));
    }

    public LiveData<List<TodoEntity>> getTodos() {
        return filteredTodos;
    }

    public void setFilters(String category, SortMode sortMode) {
        currentCategory = category;
        currentSort = sortMode;
        filterAndSort(allTodos.getValue(), category, sortMode);
    }

    private void filterAndSort(List<TodoEntity> source, String category, SortMode sortMode) {
        if (source == null) {
            filteredTodos.setValue(Collections.emptyList());
            return;
        }
        List<TodoEntity> temp = new ArrayList<>();
        for (TodoEntity entity : source) {
            if ("全部".equals(category) || entity.getCategory().equals(category)) {
                temp.add(entity);
            }
        }

        Comparator<TodoEntity> comparator;
        switch (sortMode) {
            case PRIORITY:
                comparator = Comparator.comparingInt(TodoEntity::getPriority);
                break;
            case CREATED:
                comparator = (a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt());
                break;
            case DUE_DATE:
            default:
                comparator = Comparator.comparingLong(TodoEntity::getDueDateMillis);
                break;
        }
        temp.sort(comparator);
        filteredTodos.setValue(temp);
    }

    public void insert(TodoEntity entity) {
        repository.insert(entity);
    }

    public void update(TodoEntity entity) {
        repository.update(entity);
    }

    public void delete(TodoEntity entity) {
        repository.delete(entity);
    }

    public LiveData<TodoEntity> getTodo(long id) {
        return repository.getById(id);
    }
}

