package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.data.NoteEntity;
import com.example.todolist.data.NoteWithChecklist;
import com.example.todolist.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;
    private final LiveData<List<NoteWithChecklist>> notes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        notes = repository.getNotes();
    }

    public LiveData<List<NoteWithChecklist>> getNotes() {
        return notes;
    }

    public void delete(NoteEntity note) {
        repository.delete(note);
    }
}

