package com.example.todolist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.ChecklistItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ChecklistPreviewAdapter extends RecyclerView.Adapter<ChecklistPreviewAdapter.ChecklistViewHolder> {

    private final List<ChecklistItemEntity> items = new ArrayList<>();

    public void submitList(List<ChecklistItemEntity> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checklist_preview, parent, false);
        return new ChecklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.min(items.size(), 3);
    }

    static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;
        private final TextView textView;

        ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checklistItemCheck);
            textView = itemView.findViewById(R.id.checklistItemText);
        }

        void bind(ChecklistItemEntity entity) {
            checkBox.setChecked(entity.isCompleted());
            textView.setText(entity.getContent());
        }
    }
}

