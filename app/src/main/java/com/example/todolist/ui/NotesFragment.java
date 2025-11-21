package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.adapters.NoteCardAdapter;
import com.example.todolist.data.NoteEntity;
import com.example.todolist.data.NoteWithChecklist;
import com.example.todolist.viewmodel.NoteViewModel;

public class NotesFragment extends Fragment implements NoteCardAdapter.NoteActionListener {

    private NoteViewModel viewModel;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.noteRecyclerView);
        emptyView = view.findViewById(R.id.emptyNotesView);

        NoteCardAdapter adapter = new NoteCardAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        viewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            adapter.submitList(notes);
            emptyView.setVisibility(notes == null || notes.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onEdit(NoteWithChecklist note) {
        Intent intent = new Intent(requireContext(), AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.note.getNoteId());
        startActivity(intent);
    }

    @Override
    public void onDelete(NoteEntity entity) {
        viewModel.delete(entity);
    }
}

