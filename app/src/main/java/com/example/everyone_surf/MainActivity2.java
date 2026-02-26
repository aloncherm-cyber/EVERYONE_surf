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

public class MainActivity2 extends AppCompatActivity {

    Button btnBeaches, btnClubs, btnShops, btnLessons, btnGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

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

        // === BUTTON ACTIONS ===
        btnBeaches.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Toast.makeText(MainActivity2.this, "חופים בישראל", Toast.LENGTH_SHORT).show();
                                              Intent go = new Intent(MainActivity2.this,Register_instructor.class);
                                              startActivity(go);
                                          }


                                      });




        btnClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity2.this, "מועדוני גלישה", Toast.LENGTH_SHORT).show();

            }

        });

        btnShops.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(MainActivity2.this, "חנויות גלישה", Toast.LENGTH_SHORT).show();
                                        }
                                    }

        );

        btnLessons.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent go = new Intent(MainActivity2.this, activity_select_region.class);
                                              startActivity(go);

                                              Toast.makeText(MainActivity2.this, "שיעורי גלישה", Toast.LENGTH_SHORT).show();
                                          }
                                      });



        btnGuide.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent go = new Intent(MainActivity2.this, activity_add_lesson.class);
                                            startActivity(go);
                                            Toast.makeText(MainActivity2.this, "הוספת שיעור", Toast.LENGTH_SHORT).show();
                                        }
                                    }

        );
    }
}
