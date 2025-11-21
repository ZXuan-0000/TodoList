package com.example.todolist.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String EXTRA_TODO_ID = "extra_todo_id";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESCRIPTION = "extra_description";

    @Override
    public void onReceive(Context context, Intent intent) {
        long todoId = intent.getLongExtra(EXTRA_TODO_ID, 0);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String description = intent.getStringExtra(EXTRA_DESCRIPTION);
        NotificationUtils.showReminder(context, todoId, title, description);
    }
}

