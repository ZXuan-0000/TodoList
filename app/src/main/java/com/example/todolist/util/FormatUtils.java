package com.example.todolist.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FormatUtils {

    private FormatUtils() {
    }

    public static String toDate(long millis) {
        if (millis <= 0) {
            return "无截止日期";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return "截止: " + dateFormat.format(new Date(millis));
    }

    public static String reminderLabel(Long millis) {
        if (millis == null || millis <= 0) {
            return "无提醒";
        }
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return "提醒: " + dateTimeFormat.format(new Date(millis));
    }

    public static String priorityLabel(int priority) {
        switch (priority) {
            case 1:
                return "优先级：高";
            case 2:
                return "优先级：中";
            case 3:
            default:
                return "优先级：低";
        }
    }

    public static boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
    }
}

