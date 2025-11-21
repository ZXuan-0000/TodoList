package com.example.todolist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    long insertNote(NoteEntity note);

    @Update
    void updateNote(NoteEntity note);

    @Delete
    void deleteNote(NoteEntity note);

    @Insert
    void insertChecklistItems(List<ChecklistItemEntity> items);

    @Update
    void updateChecklistItem(ChecklistItemEntity item);

    @Delete
    void deleteChecklistItem(ChecklistItemEntity item);

    @Query("DELETE FROM checklist_items WHERE note_id = :noteId")
    void deleteChecklistForNote(long noteId);

    @Transaction
    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    LiveData<List<NoteWithChecklist>> getNotes();

    @Transaction
    @Query("SELECT * FROM notes WHERE note_id = :id LIMIT 1")
    LiveData<NoteWithChecklist> getNote(long id);
}

