package com.example.everyone_surf;

import android.os.Bundle;
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
        btnBeaches.setOnClickListener(v ->
                Toast.makeText(MainActivity2.this, "חופים בישראל", Toast.LENGTH_SHORT).show()
        );

        btnClubs.setOnClickListener(v ->
                Toast.makeText(MainActivity2.this, "מועדוני גלישה", Toast.LENGTH_SHORT).show()
        );

        btnShops.setOnClickListener(v ->
                Toast.makeText(MainActivity2.this, "חנויות גלישה", Toast.LENGTH_SHORT).show()
        );

        btnLessons.setOnClickListener(v ->
                Toast.makeText(MainActivity2.this, "שיעורי גלישה", Toast.LENGTH_SHORT).show()
        );

        btnGuide.setOnClickListener(v ->
                Toast.makeText(MainActivity2.this, "מדריך למתחילים", Toast.LENGTH_SHORT).show()
        );
    }
}
