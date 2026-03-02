package com.example.everyone_surf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Activity extends AppCompatActivity {

    Button btnBeaches, btnClubs, btnShops, btnLessons, btnGuide, btnRequestInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === FIND THE BUTTONS ===
        btnBeaches = findViewById(R.id.btnBeaches);
        btnClubs = findViewById(R.id.btnClubs);
        btnShops = findViewById(R.id.btnShops);
        btnLessons = findViewById(R.id.btnLessons);
        btnGuide = findViewById(R.id.btnGuide);
        btnRequestInstructor = findViewById(R.id.btnRequestInstructor);

        // === BUTTON ACTIONS ===
        btnBeaches.setOnClickListener(v ->
                Toast.makeText(User_Activity.this, "חופים בישראל", Toast.LENGTH_SHORT).show()
        );




        btnClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(User_Activity.this, "מועדוני גלישה", Toast.LENGTH_SHORT).show();

            }

        });

        btnShops.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(User_Activity.this, "חנויות גלישה", Toast.LENGTH_SHORT).show();
                                        }
                                    }

        );

        btnLessons.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent go = new Intent(User_Activity.this, activity_select_region.class);
                                              startActivity(go);

                                              Toast.makeText(User_Activity.this, "שיעורי גלישה", Toast.LENGTH_SHORT).show();
                                          }
                                      });



        btnGuide.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent go = new Intent(User_Activity.this, activity_add_lesson.class);
                                            startActivity(go);
                                            Toast.makeText(User_Activity.this, "הוספת שיעור", Toast.LENGTH_SHORT).show();
                                        }
                                    }

        );


        btnRequestInstructor.setOnClickListener(v -> {
            Intent go = new Intent(User_Activity.this, Request_Instructor.class);
            startActivity(go);
        });
    }
}
