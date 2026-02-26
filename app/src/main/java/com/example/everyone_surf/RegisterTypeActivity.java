package com.example.everyone_surf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterTypeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStudent, btnInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_type);

        btnStudent = findViewById(R.id.btnStudentRegister);
        btnInstructor = findViewById(R.id.btnInstructorRegister);

        btnStudent.setOnClickListener(this);
        btnInstructor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btnStudent) {
            startActivity(new Intent(this, Register.class));
        }

        if (v == btnInstructor) {
            startActivity(new Intent(this, Register_instructor.class));
        }
    }
}