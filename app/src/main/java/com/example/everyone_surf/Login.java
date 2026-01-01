package com.example.everyone_surf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everyone_surf.services.DatabaseService;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView goToRegister;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        goToRegister = findViewById(R.id.goToRegister);
        databaseService=DatabaseService.getInstance();

        btnLogin.setOnClickListener(v -> {
            String email = loginEmail.getText().toString();
            String password = loginPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseService.LoginUser(email, password, new DatabaseService.DatabaseCallback<String>() {
                @Override
                public void onCompleted(String uid) {
                    Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, MainActivity2.class));
//            finish();

                }

                @Override
                public void onFailed(Exception e) {

                }
            });




        });

        goToRegister.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
        });
    }
}
