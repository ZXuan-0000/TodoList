package com.example.todolist.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.ChecklistItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ChecklistEditAdapter extends RecyclerView.Adapter<ChecklistEditAdapter.ChecklistEditViewHolder> {

    public interface ItemChangedListener {
        void onItemsChanged(List<ChecklistItemEntity> items);
    }

    private final List<ChecklistItemEntity> items = new ArrayList<>();
    private final ItemChangedListener listener;

    public ChecklistEditAdapter(ItemChangedListener listener) {
        this.listener = listener;
    }

    public void submitList(List<ChecklistItemEntity> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
        notifyListener();
    }

    public void addEmptyItem() {
        items.add(new ChecklistItemEntity(0, "", false, items.size()));
        notifyItemInserted(items.size() - 1);
        notifyListener();
    }

    private void notifyListener() {
        if (listener != null) {
            listener.onItemsChanged(new ArrayList<>(items));
        }
    }

    @NonNull
    @Override
    public ChecklistEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checklist_edit, parent, false);
        return new ChecklistEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistEditViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ChecklistEditViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final EditText editText;
        private final ImageButton removeButton;
        private TextWatcher watcher;

        ChecklistEditViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.editChecklistCheck);
            editText = itemView.findViewById(R.id.editChecklistText);
            removeButton = itemView.findViewById(R.id.removeChecklistItem);
        }

        void bind(ChecklistItemEntity entity) {
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(entity.isCompleted());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                entity.setCompleted(isChecked);
                notifyListener();
            });

            if (watcher != null) {
                editText.removeTextChangedListener(watcher);
            }
            editText.setText(entity.getContent());
            watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entity.setContent(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    notifyListener();
                }
            };
            editText.addTextChangedListener(watcher);

            removeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyListener();
                }
            });
        }
    }
}

