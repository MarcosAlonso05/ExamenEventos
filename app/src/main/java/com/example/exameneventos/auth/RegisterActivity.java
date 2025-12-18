package com.example.exameneventos.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exameneventos.MainActivity;
import com.example.exameneventos.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmailRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        tvGoToLogin.setOnClickListener(v -> {
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(RegisterActivity.this,
                        "La contraseña debe tener: min 8 caracteres, 1 Mayúscula, 1 minúscula y 1 número.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            registerUser(email, password);
        });
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
        return pattern.matcher(password).matches();
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error en registro: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}