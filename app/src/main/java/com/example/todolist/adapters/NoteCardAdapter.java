package com.example.todolist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.ChecklistItemEntity;
import com.example.todolist.data.NoteEntity;
import com.example.todolist.data.NoteWithChecklist;

import java.util.ArrayList;
import java.util.List;

public class NoteCardAdapter extends RecyclerView.Adapter<NoteCardAdapter.NoteCardViewHolder> {

    public interface NoteActionListener {
        void onEdit(NoteWithChecklist note);

        void onDelete(NoteEntity entity);
    }

    private final List<NoteWithChecklist> notes = new ArrayList<>();
    private final NoteActionListener listener;

    public NoteCardAdapter(NoteActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<NoteWithChecklist> newNotes) {
        notes.clear();
        if (newNotes != null) {
            notes.addAll(newNotes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note_card, parent, false);
        return new NoteCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteCardViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteCardViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView body;
        private final TextView summary;
        private final RecyclerView checklistPreview;
        private final ImageButton editButton;
        private final ImageButton deleteButton;
        private final ChecklistPreviewAdapter previewAdapter;

        NoteCardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noteTitle);
            body = itemView.findViewById(R.id.noteBody);
            summary = itemView.findViewById(R.id.noteChecklistSummary);
            checklistPreview = itemView.findViewById(R.id.checklistPreview);
            editButton = itemView.findViewById(R.id.noteEditButton);
            deleteButton = itemView.findViewById(R.id.noteDeleteButton);
            previewAdapter = new ChecklistPreviewAdapter();
            checklistPreview.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            checklistPreview.setAdapter(previewAdapter);
        }

        void bind(NoteWithChecklist noteWithChecklist) {
            title.setText(noteWithChecklist.note.getTitle());
            body.setText(noteWithChecklist.note.getBody());

            List<ChecklistItemEntity> items = noteWithChecklist.items;
            int completed = 0;
            if (items != null) {
                for (ChecklistItemEntity item : items) {
                    if (item.isCompleted()) {
                        completed++;
                    }
                }
            }
            summary.setText(String.format(java.util.Locale.getDefault(),
                    "子任务完成：%d/%d", completed, items == null ? 0 : items.size()));

            previewAdapter.submitList(items);

            editButton.setOnClickListener(v -> listener.onEdit(noteWithChecklist));
            deleteButton.setOnClickListener(v -> listener.onDelete(noteWithChecklist.note));
        }
    }
}

