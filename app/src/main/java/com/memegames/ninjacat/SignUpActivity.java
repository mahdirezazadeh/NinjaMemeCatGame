package com.memegames.ninjacat;

import static com.memegames.ninjacat.CatGameDataBaseHelper.checkUsernameConstraint;
import static com.memegames.ninjacat.CatGameDataBaseHelper.signup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    Bitmap selectedImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        Button btnLogin = findViewById(R.id.btnSignUpa);
        ImageButton btnAddPic = findViewById(R.id.btnAddImage);


        btnLogin.setOnClickListener(view -> {
            String password = loadTextStringFromEditText(R.id.passwordSignUpEditText);
            String username = loadTextStringFromEditText(R.id.usernameSignUpEditText).toLowerCase(Locale.ROOT);
            String passwordConfirm = loadTextStringFromEditText(R.id.passwordConfirmEditText);
            if (checkConstraints(username, password, passwordConfirm)) {

                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                byte[] img = byteArray.toByteArray();

                signup(username, password, img, this);
                Intent intent = new Intent(SignUpActivity.this, LevelsListActivity.class);
                intent.putExtra("username", username);
                SignUpActivity.this.startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Username is already taken!", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
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

    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        }
                        catch (IOException e) {e.printStackTrace();
                        }
                        ImageView imageView = findViewById(R.id.userProfileImage);
                        imageView.setImageBitmap(selectedImageBitmap);
                    }
                }
            });

}