package com.memegames.ninjacat;

import static com.memegames.ninjacat.CatGameDataBaseHelper.authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(view -> {
            String password = loadTextStringFromEditText(R.id.passwordEditText);
            String username = loadTextStringFromEditText(R.id.usernameEditText).toLowerCase(Locale.ROOT);
            if (authenticate(username, password, this)) {
                Intent intent = new Intent(LoginActivity.this, LevelsActivity.class);
                intent.putExtra("username", username);
                LoginActivity.this.startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Username or password is wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSignUp.setOnClickListener((view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            LoginActivity.this.startActivity(intent);
        }));
    }

    private String loadTextStringFromEditText(int id) {
        EditText editText = findViewById(id);
        return editText.getText().toString();
    }
}