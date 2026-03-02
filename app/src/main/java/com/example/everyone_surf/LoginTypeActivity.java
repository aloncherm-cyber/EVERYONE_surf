package com.example.everyone_surf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginTypeActivity extends AppCompatActivity {

    public static final String EXTRA_ROLE = "ROLE";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_INSTRUCTOR = "INSTRUCTOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_type);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnUser = findViewById(R.id.btnUserLogin);
        Button btnInstructor = findViewById(R.id.btnInstructorLogin);

        btnUser.setOnClickListener(v -> openLogin(ROLE_USER));
        btnInstructor.setOnClickListener(v -> openLogin(ROLE_INSTRUCTOR));
    }

    private void openLogin(String role) {
        Intent intent = new Intent(LoginTypeActivity.this, Login.class);
        intent.putExtra(EXTRA_ROLE, role);
        startActivity(intent);
    }
}