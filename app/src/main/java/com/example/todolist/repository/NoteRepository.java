package com.example.todolist.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.todolist.data.AppDatabase;
import com.example.todolist.data.ChecklistItemEntity;
import com.example.todolist.data.NoteDao;
import com.example.todolist.data.NoteEntity;
import com.example.todolist.data.NoteWithChecklist;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteRepository {

    private final NoteDao noteDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public NoteRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        this.noteDao = database.noteDao();
    }

    public LiveData<List<NoteWithChecklist>> getNotes() {
        return noteDao.getNotes();
    }

    public LiveData<NoteWithChecklist> getNote(long id) {
        return noteDao.getNote(id);
    }

    public void insert(NoteEntity note, List<ChecklistItemEntity> items) {
        executor.execute(() -> {
            long noteId = noteDao.insertNote(note);
            if (items != null && !items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    ChecklistItemEntity item = items.get(i);
                    item.setNoteId(noteId);
                    item.setPosition(i);
                }
                noteDao.insertChecklistItems(items);
            }
        });
    }

    public void update(NoteEntity note, List<ChecklistItemEntity> items) {
        executor.execute(() -> {
            noteDao.updateNote(note);
            noteDao.deleteChecklistForNote(note.getNoteId());
            if (items != null && !items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    ChecklistItemEntity item = items.get(i);
                    item.setNoteId(note.getNoteId());
                    item.setPosition(i);
                }
                noteDao.insertChecklistItems(items);
            }
        });
    }

    public void delete(NoteEntity note) {
        executor.execute(() -> noteDao.deleteNote(note));
    }
}

