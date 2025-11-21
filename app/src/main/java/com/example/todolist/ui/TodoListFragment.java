package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.adapters.TodoAdapter;
import com.example.todolist.data.TodoEntity;
import com.example.todolist.notifications.ReminderScheduler;
import com.example.todolist.viewmodel.TodoViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

public class TodoListFragment extends Fragment implements TodoAdapter.TodoActionListener {

    private TodoViewModel viewModel;
    private TextView emptyView;
    private final String[] categories = {"全部", "工作", "学习", "生活", "个人"};
    private final String[] sortModes = {"按截止日期", "按优先级", "按创建时间"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.todoRecyclerView);
        emptyView = view.findViewById(R.id.emptyView);
        MaterialAutoCompleteTextView categoryFilter = view.findViewById(R.id.categoryFilter);
        MaterialAutoCompleteTextView sortFilter = view.findViewById(R.id.sortFilter);

        TodoAdapter adapter = new TodoAdapter(this);
        recyclerView.setAdapter(adapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, categories);
        categoryFilter.setAdapter(categoryAdapter);
        categoryFilter.setText(categories[0], false);

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, sortModes);
        sortFilter.setAdapter(sortAdapter);
        sortFilter.setText(sortModes[0], false);

        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        viewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
            adapter.submitList(todos);
            emptyView.setVisibility(todos == null || todos.isEmpty() ? View.VISIBLE : View.GONE);
        });
        viewModel.setFilters(categories[0], TodoViewModel.SortMode.DUE_DATE);

        categoryFilter.setOnItemClickListener((parent, v, position, id) ->
                viewModel.setFilters(categories[position], mapSort(sortFilter.getText().toString())));

        sortFilter.setOnItemClickListener((parent, v, position, id) ->
                viewModel.setFilters(categoryFilter.getText().toString(), mapSort(sortModes[position])));
    }

    private TodoViewModel.SortMode mapSort(String label) {
        if ("按优先级".equals(label)) {
            return TodoViewModel.SortMode.PRIORITY;
        } else if ("按创建时间".equals(label)) {
            return TodoViewModel.SortMode.CREATED;
        } else {
            return TodoViewModel.SortMode.DUE_DATE;
        }
    }

    @Override
    public void onToggleComplete(TodoEntity entity, boolean completed) {
        entity.setCompleted(completed);
        viewModel.update(entity);
        if (completed) {
            ReminderScheduler.cancelReminder(requireContext(), entity.getId());
        } else {
            ReminderScheduler.scheduleReminder(requireContext(), entity);
        }
    }

    @Override
    public void onEdit(TodoEntity entity) {
        Intent intent = new Intent(requireContext(), AddEditTodoActivity.class);
        intent.putExtra(AddEditTodoActivity.EXTRA_TODO_ID, entity.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(TodoEntity entity) {
        viewModel.delete(entity);
        ReminderScheduler.cancelReminder(requireContext(), entity.getId());
    }
}

