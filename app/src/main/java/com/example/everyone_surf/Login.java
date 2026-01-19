package com.example.everyone_surf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
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

    SharedPreferences sharedPreferences;

    public static final String mysharedPrefences = "myPref";
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(mysharedPrefences, Context.MODE_PRIVATE);


        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        goToRegister = findViewById(R.id.goToRegister);
        databaseService = DatabaseService.getInstance();

        email=sharedPreferences.getString("email","");
        password=sharedPreferences.getString("password","");
        loginEmail.setText(email);
        loginPassword.setText(password);


        btnLogin.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            // בדיקת קלט בסיסית
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginEmail.setError("נא להזין אימייל תקין");
                return;
            }

            if (password.isEmpty()) {
                loginPassword.setError("נא להזין סיסמה");
                return;
            }

            // ניסיון התחברות
            databaseService.LoginUser(email, password, new DatabaseService.DatabaseCallback<String>() {
                @Override
                public void onCompleted(String uid) {


                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("email", email);
                    editor.putString("password", password);

                    editor.commit();

                    Toast.makeText(Login.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity2.class);
                    startActivity(intent);
                    finish(); // סוגר את מסך ההתחברות כדי שלא יחזרו אליו בכפתור 'אחורה'
                }

                @Override
                public void onFailed(Exception e) {
                    // כאן אנחנו מטפלים במקרה של שגיאה (משתמש לא קיים או סיסמה שגויה)
                    Toast.makeText(Login.this, "משתמש לא נמצא או פרטים שגויים. אולי כדאי להירשם?", Toast.LENGTH_LONG).show();
                }
            });
        });

        goToRegister.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
        });
    }
}