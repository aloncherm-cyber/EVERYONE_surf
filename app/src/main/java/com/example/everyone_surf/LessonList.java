package com.example.everyone_surf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyone_surf.Adapters.LessonAdapter;
import com.example.everyone_surf.model.Lesson;
import com.example.everyone_surf.services.DatabaseService;

import java.util.ArrayList;

public class LessonList extends AppCompatActivity {


    private static final String TAG = "LessonsListActivity";
    private LessonAdapter lessonAdapter;
    private TextView tvLessonCount;

    DatabaseService  databaseService;

    ArrayList<Lesson>lessonArrayList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_list);

        databaseService=DatabaseService.getInstance();

        RecyclerView lessonsList = findViewById(R.id.rvLessons);

        lessonsList.setLayoutManager(new LinearLayoutManager(this));
        lessonAdapter = new LessonAdapter(lessonArrayList, new LessonAdapter.OnLessonClickListener() {
            @Override
            public void onLessonClick(Lesson lesson) {
                // Handle lesson click
                Log.d(TAG, "Lesson clicked: " + lesson);
                Intent intent = new Intent(LessonList.this, LessonProfileActivity.class);
               intent.putExtra("LESSON_UID", lesson.getLessonId().toString());
                startActivity(intent);
            }

            @Override
            public void onLongLessonClick(Lesson lesson) {
                // Handle long lesson click
                Log.d(TAG, "Lesson long clicked: " + lesson);
            }
        });
        lessonsList.setAdapter(lessonAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseService.getLessonList(new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(List<Lesson> lessons) {
                lessonAdapter.setLessonList(lessons);
                tvLessonCount.setText("Total lessons: " + lessons.size());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to get lessons list", e);
            }
        });
    }


}
