package com.example.everyone_surf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everyone_surf.model.User;
import com.example.everyone_surf.services.DatabaseService;

public class Register extends AppCompatActivity {

    EditText fnameInput, lnameInput, phoneInput, genderInput, ageInput, emailInput, passwordInput;
    Button btnRegister;
    TextView goToLogin;

    DatabaseService databaseService;
    SharedPreferences sharedPreferences;

    public static final String mysharedPrefences = "myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        databaseService = DatabaseService.getInstance();
        sharedPreferences = getSharedPreferences(mysharedPrefences, Context.MODE_PRIVATE);

        fnameInput = findViewById(R.id.registerFname);
        lnameInput = findViewById(R.id.registerLname);
        phoneInput = findViewById(R.id.registerPhone);
        genderInput = findViewById(R.id.registerGender);
        ageInput = findViewById(R.id.registerAge);
        emailInput = findViewById(R.id.registerEmail);
        passwordInput = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.btnRegister);
        goToLogin = findViewById(R.id.goToLogin);

        btnRegister.setOnClickListener(v -> {

            String fname = fnameInput.getText().toString();
            String lname = lnameInput.getText().toString();
            String phone = phoneInput.getText().toString();
            String gender = genderInput.getText().toString();
            String age = ageInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "נא למלא אימייל וסיסמה", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User("1", fname, lname, phone, gender, age, email, password);

            databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<String>() {
                @Override
                public void onCompleted(String uid) {

                    user.setId(uid);
                    Toast.makeText(Register.this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();

                    Intent go = new Intent(Register.this, MainActivity2.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(go);
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(Register.this, "שגיאה בהרשמה", Toast.LENGTH_SHORT).show();
                }
            });

        });

        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }
}
