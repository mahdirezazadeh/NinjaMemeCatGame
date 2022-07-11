package com.memegames.ninjacat;

import static com.memegames.ninjacat.CatGameDataBaseHelper.authenticate;
import static com.memegames.ninjacat.CatGameDataBaseHelper.change_log_status_to_login;
import static com.memegames.ninjacat.CatGameDataBaseHelper.getLoggedInUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        String username_checked = getLoggedInUser(this);

        if(username_checked != null) {
            Intent intent = new Intent(LoginActivity.this, LevelsListActivity.class);
            intent.putExtra("username", username_checked);
            LoginActivity.this.startActivity(intent);
        }

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(view -> {
            String password = loadTextStringFromEditText(R.id.passwordEditText);
            String username = loadTextStringFromEditText(R.id.usernameEditText).toLowerCase(Locale.ROOT);
            if (authenticate(username, password, this)) {
                Intent intent = new Intent(LoginActivity.this, LevelsListActivity.class);
                intent.putExtra("username", username);
                change_log_status_to_login(username, this);
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