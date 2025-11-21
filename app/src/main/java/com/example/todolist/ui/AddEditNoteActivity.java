package com.example.todolist.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.adapters.ChecklistEditAdapter;
import com.example.todolist.data.ChecklistItemEntity;
import com.example.todolist.data.NoteEntity;
import com.example.todolist.data.NoteWithChecklist;
import com.example.todolist.viewmodel.AddEditNoteViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddEditNoteActivity extends AppCompatActivity implements ChecklistEditAdapter.ItemChangedListener {

    public static final String EXTRA_NOTE_ID = "extra_note_id";

    private TextInputEditText titleInput;
    private TextInputEditText bodyInput;
    private RecyclerView checklistRecycler;
    private ChecklistEditAdapter checklistAdapter;
    private Button addChecklistButton;
    private Button saveButton;
    private Button voiceButton;

    private AddEditNoteViewModel viewModel;
    private long noteId = -1;
    private long createdAt = System.currentTimeMillis();
    private List<ChecklistItemEntity> items = new ArrayList<>();

    private ActivityResultLauncher<Intent> speechLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        initViews();
        setupSpeech();

        viewModel = new ViewModelProvider(this).get(AddEditNoteViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_NOTE_ID)) {
            noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1);
            setTitle(R.string.edit_note);
            viewModel.getNote(noteId).observe(this, this::populateNote);
        } else {
            setTitle(R.string.add_note);
        }

        addChecklistButton.setOnClickListener(v -> checklistAdapter.addEmptyItem());
        saveButton.setOnClickListener(v -> saveNote());
        voiceButton.setOnClickListener(v -> permissionLauncher.launch(Manifest.permission.RECORD_AUDIO));
    }

    private void initViews() {
        titleInput = findViewById(R.id.noteTitleInput);
        bodyInput = findViewById(R.id.noteBodyInput);
        checklistRecycler = findViewById(R.id.checklistEditRecycler);
        addChecklistButton = findViewById(R.id.addChecklistItemButton);
        saveButton = findViewById(R.id.saveNoteButton);
        voiceButton = findViewById(R.id.noteVoiceButton);

        checklistAdapter = new ChecklistEditAdapter(this);
        checklistRecycler.setLayoutManager(new LinearLayoutManager(this));
        checklistRecycler.setAdapter(checklistAdapter);
        checklistAdapter.addEmptyItem();
    }

    private void setupSpeech() {
        speechLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(
                                RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            bodyInput.setText(matches.get(0));
                        }
                    }
                });

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                        speechLauncher.launch(intent);
                    } else {
                        Toast.makeText(this, "需要麦克风权限以使用语音输入", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateNote(NoteWithChecklist note) {
        if (note == null) return;
        titleInput.setText(note.note.getTitle());
        bodyInput.setText(note.note.getBody());
        createdAt = note.note.getCreatedAt();
        if (note.items != null && !note.items.isEmpty()) {
            items = new ArrayList<>(note.items);
            checklistAdapter.submitList(items);
        } else {
            checklistAdapter.addEmptyItem();
        }
    }

    private void saveNote() {
        String title = titleInput.getText() != null ? titleInput.getText().toString() : "";
        if (TextUtils.isEmpty(title)) {
            titleInput.setError("标题不能为空");
            return;
        }
        String body = bodyInput.getText() != null ? bodyInput.getText().toString() : "";

        NoteEntity noteEntity = new NoteEntity(title, body, createdAt > 0 ? createdAt : System.currentTimeMillis());

        List<ChecklistItemEntity> filteredItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            ChecklistItemEntity item = items.get(i);
            if (!TextUtils.isEmpty(item.getContent())) {
                filteredItems.add(new ChecklistItemEntity(noteId, item.getContent(), item.isCompleted(), i));
            }
        }

        if (noteId > 0) {
            noteEntity.setNoteId(noteId);
            viewModel.update(noteEntity, filteredItems);
        } else {
            viewModel.insert(noteEntity, filteredItems);
        }
        finish();
    }

    @Override
    public void onItemsChanged(List<ChecklistItemEntity> items) {
        this.items = items;
    }
}

