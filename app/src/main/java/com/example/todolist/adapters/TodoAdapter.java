package com.example.todolist.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.TodoEntity;
import com.example.todolist.util.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    public interface TodoActionListener {
        void onToggleComplete(TodoEntity entity, boolean completed);

        void onEdit(TodoEntity entity);

        void onDelete(TodoEntity entity);
    }

    private final TodoActionListener listener;
    private final List<TodoEntity> data = new ArrayList<>();

    public TodoAdapter(TodoActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<TodoEntity> newData) {
        data.clear();
        if (newData != null) {
            data.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final TextView title;
        private final TextView description;
        private final TextView category;
        private final TextView dueDate;
        private final TextView priority;
        private final TextView reminder;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todoCheckBox);
            title = itemView.findViewById(R.id.todoTitle);
            description = itemView.findViewById(R.id.todoDescription);
            category = itemView.findViewById(R.id.todoCategory);
            dueDate = itemView.findViewById(R.id.todoDueDate);
            priority = itemView.findViewById(R.id.todoPriority);
            reminder = itemView.findViewById(R.id.todoReminder);
            editButton = itemView.findViewById(R.id.todoEditButton);
            deleteButton = itemView.findViewById(R.id.todoDeleteButton);
        }

        void bind(TodoEntity entity) {
            title.setText(entity.getTitle());
            String desc = entity.getDescription();
            if (TextUtils.isEmpty(desc)) {
                description.setVisibility(View.GONE);
            } else {
                description.setVisibility(View.VISIBLE);
                description.setText(desc);
            }
            category.setText(entity.getCategory());
            dueDate.setText(FormatUtils.toDate(entity.getDueDateMillis()));
            priority.setText(FormatUtils.priorityLabel(entity.getPriority()));
            reminder.setText(FormatUtils.reminderLabel(entity.getReminderTimeMillis()));
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(entity.isCompleted());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    listener.onToggleComplete(entity, isChecked));
            editButton.setOnClickListener(v -> listener.onEdit(entity));
            deleteButton.setOnClickListener(v -> listener.onDelete(entity));
        }
    }
}

