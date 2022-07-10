package com.memegames.ninjacat;

import static com.memegames.ninjacat.CatGameDataBaseHelper.checkUsernameConstraint;
import static com.memegames.ninjacat.CatGameDataBaseHelper.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        Button btnLogin = findViewById(R.id.btnSignUpa);

        btnLogin.setOnClickListener(view -> {
            String password = loadTextStringFromEditText(R.id.passwordSignUpEditText);
            String username = loadTextStringFromEditText(R.id.usernameSignUpEditText).toLowerCase(Locale.ROOT);
            String passwordConfirm = loadTextStringFromEditText(R.id.passwordConfirmEditText);
            if (checkConstraints(username, password, passwordConfirm)) {
                signup(username, password, this);
                Intent intent = new Intent(SignUpActivity.this, GameActivity.class);
                intent.putExtra("username", username);
                SignUpActivity.this.startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Username is already taken!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkConstraints(String username, String password, String passwordConfirm) {
        if(!password.equals(passwordConfirm))
            return false;
        return checkUsernameConstraint(username, this);
    }


    private String loadTextStringFromEditText(int id) {
        EditText editText = findViewById(id);
        return editText.getText().toString();
    }
}