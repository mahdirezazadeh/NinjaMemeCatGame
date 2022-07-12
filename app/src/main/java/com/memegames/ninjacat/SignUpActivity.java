package com.memegames.ninjacat;

import static com.memegames.ninjacat.CatGameDataBaseHelper.checkUsernameConstraint;
import static com.memegames.ninjacat.CatGameDataBaseHelper.signup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    Bitmap selectedImageBitmap = null;
    ImageView imageView = null;
    Uri selectedImageUri = null;
    EditText userName = null;
    EditText userPass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState != null) {
//
//            if (savedInstanceState.getString("img_saved") != null) {
//                String image_uri_string = savedInstanceState.getString("img_saved");
//                Uri image_uri = Uri.parse(image_uri_string);
//                Toast toast = Toast.makeText(getApplicationContext(), "loading saved image",Toast.LENGTH_SHORT);
//                toast.show();
//
//                try {
//                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                imageView.setImageBitmap(selectedImageBitmap);
//            }
//
//            if (savedInstanceState.getString("use_name") != null) {
//                userName.setText(savedInstanceState.getString("user_name").toString());
//            }
//
//            if (savedInstanceState.getString("use_pass") != null) {
//                userName.setText(savedInstanceState.getString("user_pass").toString());
//            }
//
//        }

        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        Button btnSignUp = findViewById(R.id.btnSignUp);
        ImageButton btnAddPic = findViewById(R.id.btnAddImage);
        imageView = findViewById(R.id.userProfileImage);
        userName = (EditText) findViewById(R.id.usernameSignUpEditText);
        userPass = (EditText) findViewById(R.id.passwordSignUpEditText);
        
        btnSignUp.setOnClickListener(view -> {
            String password = userPass.getText().toString();
            String username = userPass.getText().toString();
//            String passwordConfirm = loadTextStringFromEditText(R.id.passwordConfirmEditText);
            String passwordConfirm = password;

            if(!password.equals("") && !username.equals("") && !passwordConfirm.equals("")) {
                if (checkConstraints(username, password, passwordConfirm)) {
                    byte[] img;
                    try{
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                        img = byteArray.toByteArray();
                    }catch (Exception ignore){
                        img = null;
                    }

                    signup(username, password, img, this);
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.putExtra("username", username);
                    SignUpActivity.this.startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Username is already taken!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Username or Password or Profile Image can not be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//
//        if (selectedImageUri != null) {
//            savedInstanceState.putString("img_saved", selectedImageUri.toString());
//            savedInstanceState.put
//        }
//
//        if (savedInstanceState.getString("use_name") != null) {
//            savedInstanceState.putString("user_name", userName.getText().toString());
//        }
//
//        if (savedInstanceState.getString("use_pass") != null) {
//            savedInstanceState.putString("user_pass", userPass.getText().toString());
//        }
//    }

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
                        selectedImageUri = data.getData();
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        }
                        catch (IOException e) {e.printStackTrace();
                        }
                        imageView.setImageBitmap(selectedImageBitmap);
                    }
                }
            });

}