package com.example.todolist.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "checklist_items",
        foreignKeys = @ForeignKey(
                entity = NoteEntity.class,
                parentColumns = "note_id",
                childColumns = "note_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("note_id")})
public class ChecklistItemEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "note_id")
    private long noteId;

    private String content;

    private boolean completed;

    private int position;

    public ChecklistItemEntity(long noteId, String content, boolean completed, int position) {
        this.noteId = noteId;
        this.content = content;
        this.completed = completed;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

