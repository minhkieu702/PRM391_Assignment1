package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanager.databinding.ActivityAddTaskBinding;

public class AddTaskActivity extends AppCompatActivity {
    private EditText editTextTaskName;
    private TimePicker timePicker;
    private Button btnAddOrUpdateTask;
    private int taskPosition = -2;
    private ActivityAddTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextTaskName = binding.editTextTaskName;
        timePicker = binding.timePicker;
        timePicker.setIs24HourView(true);
        btnAddOrUpdateTask = binding.btnAddTask;

        Intent intent = getIntent();
        taskPosition = intent.getIntExtra("task_position", -2);

        if (intent != null && taskPosition != -2) {
            String taskTime = intent.getStringExtra("task_time");
            String taskName = intent.getStringExtra("task_name");

            editTextTaskName.setText(taskName);

            // Tách giờ và phút
            String[] hourMinuteParts = taskTime.split(":");
            int hour = Integer.parseInt(hourMinuteParts[0]);
            int minute = Integer.parseInt(hourMinuteParts[1]);

            // Cài đặt TimePicker ở chế độ 12 giờ
            timePicker.setIs24HourView(true);

            timePicker.setHour(hour);
            timePicker.setMinute(minute);

            btnAddOrUpdateTask.setText("Update Task");
        }
        // Xử lý khi người dùng nhấn nút Add
        binding.btnAddTask.setOnClickListener(v -> {
            String taskName = binding.editTextTaskName.getText().toString();
            int hour = binding.timePicker.getHour();
            int minute = binding.timePicker.getMinute();
            String time = String.format("%02d:%02d", hour, minute);
            // Trả dữ liệu task về TaskListActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("task_name", taskName);
            resultIntent.putExtra("task_time", time);
            resultIntent.putExtra("task_position", taskPosition);  // Include position for both add and update
            setResult(RESULT_OK, resultIntent);
            finish(); // Kết thúc AddTaskActivity và quay về TaskListActivity
        });
    }
}