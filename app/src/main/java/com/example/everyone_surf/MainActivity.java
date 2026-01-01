package com.example.everyone_surf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button registerbtn, loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        finds();
    }

    public void finds() {
        registerbtn = findViewById(R.id.btnRegister);
        loginbtn = findViewById(R.id.btnLoginMain);

        registerbtn.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == registerbtn) {
            Intent go = new Intent(MainActivity.this, Register.class);
            startActivity(go);
        }

        if (v == loginbtn) {
            Intent go = new Intent(MainActivity.this, Login.class);
            startActivity(go);
        }
    }
}
