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
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView goToRegister;

    DatabaseService databaseService;
    SharedPreferences sharedPreferences;

    public static final String mysharedPrefences = "myPref";

    private String selectedRole; // USER / INSTRUCTOR

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

        // ROLE שהגיע מ-LoginTypeActivity
        selectedRole = getIntent().getStringExtra(LoginTypeActivity.EXTRA_ROLE);
        if (selectedRole == null) selectedRole = LoginTypeActivity.ROLE_USER;

        // מילוי אוטומטי מהעדפה (כמו אצלך)
        loginEmail.setText(sharedPreferences.getString("email", ""));
        loginPassword.setText(sharedPreferences.getString("password", ""));

        btnLogin.setOnClickListener(v -> attemptLogin());

        goToRegister.setOnClickListener(v ->
                startActivity(new Intent(Login.this, Register.class))
        );
    }

    private void attemptLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("נא להזין אימייל תקין");
            return;
        }

        if (password.isEmpty()) {
            loginPassword.setError("נא להזין סיסמה");
            return;
        }

        // התחברות עם FirebaseAuth דרך השירות שלך
        databaseService.LoginUser(email, password, new DatabaseService.DatabaseCallback<String>() {
            @Override
            public void onCompleted(String uid) {
                // אחרי התחברות מוצלחת - בודקים ROLE מול Realtime DB
                checkRoleThenNavigate(uid, email, password);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(Login.this,
                        "משתמש לא נמצא או פרטים שגויים. אולי כדאי להירשם?",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkRoleThenNavigate(String uid, String email, String password) {

        String path = selectedRole.equals(LoginTypeActivity.ROLE_INSTRUCTOR)
                ? "instructors"
                : "users";

        databaseService.isUidExistsInPath(path, uid, new DatabaseService.DatabaseCallback<Boolean>() {
            @Override
            public void onCompleted(Boolean exists) {
                if (exists) {
                    // שמירת פרטים (כמו שהיה אצלך)
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(Login.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();

                    if (selectedRole.equals(LoginTypeActivity.ROLE_INSTRUCTOR)) {
                        startActivity(new Intent(Login.this, Instructor_Activity.class));
                    } else {
                        startActivity(new Intent(Login.this, User_Activity.class));
                    }
                    finish();

                } else {
                    // התחבר ב-Auth אבל אין לו רשומה בענף המתאים => לא מורשה לסוג הזה
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(Login.this,
                            "אין לך הרשאה להתחבר כסוג הזה",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(Login.this,
                        "שגיאת DB: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}