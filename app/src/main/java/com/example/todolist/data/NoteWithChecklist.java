package com.example.todolist.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class NoteWithChecklist {

    @Embedded
    public NoteEntity note;

    @Relation(
            parentColumn = "note_id",
            entityColumn = "note_id",
            entity = ChecklistItemEntity.class
    )
    public List<ChecklistItemEntity> items;
}

