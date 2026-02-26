package com.example.everyone_surf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.everyone_surf.model.Instructor;
import com.example.everyone_surf.model.Lesson;
import com.example.everyone_surf.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;

public class activity_add_lesson extends AppCompatActivity implements View.OnClickListener {

    EditText etTime, etDate, etprice ;
    Spinner spRegion;

    Button btnSaveLesson;

    String time , date ,stPrice , region;
    DatabaseService databaseService;

    Instructor currentInstructor;
    FirebaseAuth mAuth;
    String insId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_lesson);

     mAuth = FirebaseAuth.getInstance();
     insId=mAuth.getCurrentUser().getUid();
       initViews();

       databaseService.getInstructor(insId, new DatabaseService.DatabaseCallback<Instructor>() {
           @Override
           public void onCompleted(Instructor instructor) {
               currentInstructor=new Instructor(instructor);
           }

           @Override
           public void onFailed(Exception e) {

           }
       });
    }

    private void initViews() {

        etDate=findViewById(R.id.etDate);
        etTime=findViewById(R.id.etTime);
        etprice=findViewById(R.id.etPrice);
        spRegion=findViewById(R.id.spinner_region);
        btnSaveLesson=findViewById(R.id.btnSaveLesson);
        btnSaveLesson.setOnClickListener(this);
        databaseService=DatabaseService.getInstance();
    }

    @Override
    public void onClick(View v) {

        date=etDate.getText().toString();
        time=etTime.getText().toString();
        stPrice=etprice.getText().toString();
        double price=Double.parseDouble(stPrice);
        region=spRegion.getSelectedItem().toString();

        String lessonId=databaseService.generateLessonId();

            Lesson newLesson=new Lesson(lessonId,currentInstructor,region,date,time,price,"new");

            databaseService.createNewLesson(newLesson, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {

                }

                @Override
                public void onFailed(Exception e) {

                }
            });

    }
}