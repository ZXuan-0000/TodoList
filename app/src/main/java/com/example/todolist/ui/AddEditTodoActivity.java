package com.example.todolist.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.R;
import com.example.todolist.data.TodoEntity;
import com.example.todolist.notifications.ReminderRescheduleService;
import com.example.todolist.notifications.ReminderScheduler;
import com.example.todolist.viewmodel.AddEditTodoViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTodoActivity extends AppCompatActivity {

    public static final String EXTRA_TODO_ID = "extra_todo_id";

    private TextInputEditText titleInput;
    private TextInputEditText descriptionInput;
    private MaterialAutoCompleteTextView categoryInput;
    private MaterialAutoCompleteTextView priorityInput;
    private TextInputEditText dueDateInput;
    private TextInputEditText reminderInput;
    private CheckBox completedCheckBox;
    private Button saveButton;
    private Button voiceButton;

    private AddEditTodoViewModel viewModel;
    private long todoId = -1;
    private long createdAt = System.currentTimeMillis();
    private long dueDateMillis = 0;
    private Long reminderMillis = null;

    private final String[] categories = {"工作", "学习", "生活", "个人"};
    private final String[] priorities = {"高", "中", "低"};

    private ActivityResultLauncher<Intent> speechLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        initViews();
        setupLaunchers();

        viewModel = new ViewModelProvider(this).get(AddEditTodoViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TODO_ID)) {
            todoId = intent.getLongExtra(EXTRA_TODO_ID, -1);
            setTitle(R.string.edit_todo);
            viewModel.getTodo(todoId).observe(this, new androidx.lifecycle.Observer<TodoEntity>() {
                private boolean initialized = false;

                @Override
                public void onChanged(TodoEntity todo) {
                    if (todo != null && !initialized) {
                        initialized = true;
                        populateFields(todo);
                    }
                }
            });
        } else {
            setTitle(R.string.add_todo);
        }

        dueDateInput.setOnClickListener(v -> pickDate());
        dueDateInput.setOnLongClickListener(v -> {
            dueDateMillis = 0;
            dueDateInput.setText("");
            return true;
        });
        reminderInput.setOnClickListener(v -> pickReminder());
        reminderInput.setOnLongClickListener(v -> {
            reminderMillis = null;
            reminderInput.setText("");
            return true;
        });
        voiceButton.setOnClickListener(v -> requestVoiceInput());
        saveButton.setOnClickListener(v -> saveTodo());
    }

    private void initViews() {
        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        categoryInput = findViewById(R.id.categoryInput);
        priorityInput = findViewById(R.id.priorityInput);
        dueDateInput = findViewById(R.id.dueDateInput);
        reminderInput = findViewById(R.id.reminderInput);
        completedCheckBox = findViewById(R.id.completedCheckBox);
        saveButton = findViewById(R.id.saveButton);
        voiceButton = findViewById(R.id.voiceButton);

        android.widget.ArrayAdapter<String> categoryAdapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, categories);
        categoryInput.setAdapter(categoryAdapter);

        android.widget.ArrayAdapter<String> priorityAdapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, priorities);
        priorityInput.setAdapter(priorityAdapter);
        categoryInput.setText(categories[0], false);
        priorityInput.setText(priorities[1], false);
    }

    private void setupLaunchers() {
        speechLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(
                                RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            String text = matches.get(0);
                            descriptionInput.setText(text);
                        }
                    }
                });

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        startSpeechRecognition();
                    } else {
                        Toast.makeText(this, "需要麦克风权限以使用语音输入", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateFields(TodoEntity todo) {
        titleInput.setText(todo.getTitle());
        descriptionInput.setText(todo.getDescription());
        categoryInput.setText(todo.getCategory(), false);
        priorityInput.setText(priorityLabel(todo.getPriority()), false);
        dueDateMillis = todo.getDueDateMillis();
        reminderMillis = todo.getReminderTimeMillis();
        dueDateInput.setText(dueDateMillis > 0 ? toDateString(dueDateMillis) : "");
        reminderInput.setText(reminderMillis != null ? toDateTimeString(reminderMillis) : "");
        completedCheckBox.setChecked(todo.isCompleted());
        createdAt = todo.getCreatedAt();
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        if (dueDateMillis > 0) {
            calendar.setTimeInMillis(dueDateMillis);
        }
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth, 0, 0, 0);
                    dueDateMillis = selected.getTimeInMillis();
                    dueDateInput.setText(toDateString(dueDateMillis));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void pickReminder() {
        Calendar calendar = Calendar.getInstance();
        if (reminderMillis != null) {
            calendar.setTimeInMillis(reminderMillis);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, day);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timePicker, hourOfDay, minute) -> {
                                selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selected.set(Calendar.MINUTE, minute);
                                selected.set(Calendar.SECOND, 0);
                                reminderMillis = selected.getTimeInMillis();
                                reminderInput.setText(toDateTimeString(reminderMillis));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void requestVoiceInput() {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechLauncher.launch(intent);
    }

    private void saveTodo() {
        String title = titleInput.getText() != null ? titleInput.getText().toString() : "";
        if (TextUtils.isEmpty(title)) {
            titleInput.setError("标题不能为空");
            return;
        }

        String description = descriptionInput.getText() != null ? descriptionInput.getText().toString() : "";
        String category = categoryInput.getText() != null ? categoryInput.getText().toString() : categories[0];
        String priorityText = priorityInput.getText() != null ? priorityInput.getText().toString() : priorities[1];
        int priority = mapPriority(priorityText);
        boolean completed = completedCheckBox.isChecked();

        if (reminderMillis != null && reminderMillis <= System.currentTimeMillis()) {
            Toast.makeText(this, "提醒时间需晚于当前时间", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoEntity entity = new TodoEntity(
                title,
                description,
                category,
                priority,
                dueDateMillis,
                reminderMillis,
                completed,
                createdAt > 0 ? createdAt : System.currentTimeMillis()
        );

        if (todoId > 0) {
            entity.setId(todoId);
            viewModel.update(entity);
            handleReminder(entity);
        } else {
            long newId = viewModel.insertSync(entity);
            if (newId > 0) {
                entity.setId(newId);
                handleReminder(entity);
            } else {
                ReminderRescheduleService.enqueueWork(this);
            }
        }
        finish();
    }

    private void handleReminder(TodoEntity entity) {
        if (entity.getReminderTimeMillis() != null && !entity.isCompleted()) {
            ReminderScheduler.scheduleReminder(this, entity);
        } else {
            ReminderScheduler.cancelReminder(this, entity.getId());
        }
    }

    private String toDateString(long millis) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new java.util.Date(millis));
    }

    private String toDateTimeString(long millis) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return format.format(new java.util.Date(millis));
    }

    private int mapPriority(String label) {
        if ("高".equals(label)) return 1;
        if ("中".equals(label)) return 2;
        return 3;
    }

    private String priorityLabel(int priority) {
        switch (priority) {
            case 1:
                return "高";
            case 2:
                return "中";
            default:
                return "低";
        }
    }
}

