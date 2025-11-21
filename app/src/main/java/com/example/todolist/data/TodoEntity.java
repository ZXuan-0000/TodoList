package com.example.todolist.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class TodoEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String title;

    private String description;

    @NonNull
    private String category;

    private int priority;

    @ColumnInfo(name = "due_date")
    private long dueDateMillis;

    @ColumnInfo(name = "reminder_time")
    private Long reminderTimeMillis;

    private boolean completed;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public TodoEntity(@NonNull String title, String description, @NonNull String category,
                      int priority, long dueDateMillis, Long reminderTimeMillis, boolean completed,
                      long createdAt) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDateMillis = dueDateMillis;
        this.reminderTimeMillis = reminderTimeMillis;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getDueDateMillis() {
        return dueDateMillis;
    }

    public void setDueDateMillis(long dueDateMillis) {
        this.dueDateMillis = dueDateMillis;
    }

    public Long getReminderTimeMillis() {
        return reminderTimeMillis;
    }

    public void setReminderTimeMillis(Long reminderTimeMillis) {
        this.reminderTimeMillis = reminderTimeMillis;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

