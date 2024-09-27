package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskmanager.databinding.ActivityTaskListBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private static final int ADD_TASK_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTaskListBinding binding = ActivityTaskListBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        taskList = new ArrayList<>();
        taskList.add(new Task("Task 1", "10:00"));
        taskList.add(new Task("Task 2", "11:30"));

        binding.btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
        });
        taskAdapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onEditTaskClick(int position) {
                // Gọi đến AddTaskActivity để sửa task
                Task task = taskList.get(position);
                String name = task.getName();
                String time = task.getTime();
                Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
                intent.putExtra("task_name", name);
                intent.putExtra("task_time", time);
                intent.putExtra("task_position", position);
                startActivityForResult(intent, position);
            }

            @Override
            public void onDeleteTaskClick(int position) {
                // Xóa task
                taskList.remove(position);
                taskAdapter.notifyItemRemoved(position);
            }
        });

        binding.recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewTasks.setAdapter(taskAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String taskName = data.getStringExtra("task_name");
            String taskTime = data.getStringExtra("task_time");
            int position = data.getIntExtra("task_position", -2);

            if (position == -2) {
                // Thêm task mới
                Task newTask = new Task(taskName, taskTime);
                taskList.add(newTask);
                taskAdapter.notifyItemInserted(taskList.size() - 1);
            } else {
                // Cập nhật task hiện có
                Task updatedTask = taskList.get(position);
                updatedTask.setName(taskName);
                updatedTask.setTime(taskTime);
                taskAdapter.notifyItemChanged(position);
            }
        }
    }
}