package com.example.todolist;

import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todolist.notifications.NotificationUtils;
import com.example.todolist.ui.AddEditNoteActivity;
import com.example.todolist.ui.AddEditTodoActivity;
import com.example.todolist.ui.NotesFragment;
import com.example.todolist.ui.TodoListFragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FloatingActionButton fab;
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationUtils.createChannel(this);
        setupPermissionLauncher();
        requestNotificationPermissionIfNeeded();
        com.example.todolist.notifications.ReminderRescheduleService.enqueueWork(this);

        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.fab);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return new TodoListFragment();
                } else {
                    return new NotesFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.tab_tasks);
            } else {
                tab.setText(R.string.tab_notes);
            }
        }).attach();

        fab.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == 0) {
                startActivity(new Intent(this, AddEditTodoActivity.class));
            } else {
                startActivity(new Intent(this, AddEditNoteActivity.class));
            }
        });
    }

    private void setupPermissionLauncher() {
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    // No-op; best effort permission request
                }
        );
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }
}

