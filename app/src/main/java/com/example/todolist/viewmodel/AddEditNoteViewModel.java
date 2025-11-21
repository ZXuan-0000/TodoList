package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.data.ChecklistItemEntity;
import com.example.todolist.data.NoteEntity;
import com.example.todolist.data.NoteWithChecklist;
import com.example.todolist.repository.NoteRepository;

import java.util.List;

public class AddEditNoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;

    public AddEditNoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
    }

    public LiveData<NoteWithChecklist> getNote(long id) {
        return repository.getNote(id);
    }

    public void insert(NoteEntity note, List<ChecklistItemEntity> items) {
        repository.insert(note, items);
    }

    public void update(NoteEntity note, List<ChecklistItemEntity> items) {
        repository.update(note, items);
    }
}

