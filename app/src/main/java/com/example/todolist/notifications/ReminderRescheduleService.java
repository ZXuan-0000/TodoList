package com.example.todolist.notifications;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.todolist.data.AppDatabase;
import com.example.todolist.data.TodoDao;
import com.example.todolist.data.TodoEntity;

import java.util.List;

public class ReminderRescheduleService extends JobIntentService {

    private static final int JOB_ID = 5001;

    public static void enqueueWork(Context context) {
        enqueueWork(context, ReminderRescheduleService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        TodoDao dao = database.todoDao();
        List<TodoEntity> pending = dao.getPendingReminders(System.currentTimeMillis());
        for (TodoEntity entity : pending) {
            ReminderScheduler.scheduleReminder(getApplicationContext(), entity);
        }
    }
}

