package com.example.everyone_surf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns; // חשוב לבדיקת אימייל
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everyone_surf.model.User;
import com.example.everyone_surf.services.DatabaseService;

public class Register extends AppCompatActivity {

    EditText fnameInput, lnameInput, phoneInput, ageInput, emailInput, passwordInput;
    RadioGroup radioGroupGender;
    Button btnRegister;
    TextView goToLogin;

    DatabaseService databaseService;
    SharedPreferences sharedPreferences;

    public static final String mysharedPrefences = "myPref";
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        databaseService = DatabaseService.getInstance();
        sharedPreferences = getSharedPreferences(mysharedPrefences, Context.MODE_PRIVATE);

        // קישור רכיבים
        fnameInput = findViewById(R.id.registerFname);
        lnameInput = findViewById(R.id.registerLname);
        phoneInput = findViewById(R.id.registerPhone);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        ageInput = findViewById(R.id.registerAge);
        emailInput = findViewById(R.id.registerEmail);
        passwordInput = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.btnRegister);
        goToLogin = findViewById(R.id.goToLogin);

        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                performRegistration();
            }
        });

        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }

    // פונקציה לבדיקת כל השדות
    private boolean validateInputs() {
        String fname = fnameInput.getText().toString().trim();
        String lname = lnameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String age = ageInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // בדיקת שדות ריקים בסיסיים
        if (fname.isEmpty()) { fnameInput.setError("נא להזין שם פרטי"); return false; }
        if (lname.isEmpty()) { lnameInput.setError("נא להזין שם משפחה"); return false; }

        // בדיקת טלפון (לפחות 10 ספרות)
        if (phone.length() < 10) { phoneInput.setError("מספר טלפון לא תקין"); return false; }

        // בדיקת גיל (עד 2 ספרות)
        if (age.isEmpty() || age.length() > 2) {
            ageInput.setError("נא להזין גיל תקין (עד 2 ספרות)");
            return false;
        }

        // בדיקת אימייל תקני (תומך בכל סוגי האימיילים)
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("נא להזין אימייל תקין");
            return false;
        }

        // בדיקת סיסמה (לפחות 6 תווים)
        if (password.length() < 6) {
            passwordInput.setError("הסיסמה חייבת להכיל לפחות 6 תווים");
            return false;
        }

        // בדיקת בחירת מין
        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "נא לבחור מין", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // פונקציית ההרשמה עצמה
    private void performRegistration() {
        String fname = fnameInput.getText().toString();
        String lname = lnameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String age = ageInput.getText().toString();

         email = emailInput.getText().toString();
         password = passwordInput.getText().toString();

        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String gender = selectedRadioButton.getText().toString();

        User user = new User("1", fname, lname, phone, gender, age, email, password);

        databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<String>() {
            @Override
            public void onCompleted(String uid) {
                user.setId(uid);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("email", email);
                editor.putString("password", password);

                editor.commit();


                Toast.makeText(Register.this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
                Intent go = new Intent(Register.this, Login.class);
                go.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(go);

            }

            @Override
            public void onFailed(Exception e) {

                Log.d("TAG", "createUser:success"+ user.toString());

                Toast.makeText(Register.this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}