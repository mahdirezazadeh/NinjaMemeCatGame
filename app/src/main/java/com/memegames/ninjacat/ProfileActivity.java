package com.memegames.ninjacat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import static com.memegames.ninjacat.CatGameDataBaseHelper.get_image;
import static com.memegames.ninjacat.CatGameDataBaseHelper.change_log_status_to_logout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    Bitmap selectedImageBitmap;
    de.hdodenhof.circleimageview.CircleImageView profile;

    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        Button cancel = (Button) findViewById(R.id.cancelButton);
        Button save = (Button) findViewById(R.id.saveButton);
        Button logout = (Button) findViewById(R.id.logoutButton);
        ImageButton change_image = (ImageButton) findViewById(R.id.changeImageButton);
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        profile = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.userProfileImage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            usernameEditText.setText(username);

        }

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_SHORT);
                toast.show();
                imageChooser();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "save", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT);
                toast.show();
                change_log_status_to_logout(username, ProfileActivity.this);
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ProfileActivity.this.startActivity(intent);
            }
        });

        byte[] image_db = get_image(username, this);
        if (image_db == null) {
            Toast.makeText(getApplicationContext(), "null image", Toast.LENGTH_SHORT).show();
        }
        else {
            Bitmap bmp = BitmapFactory.decodeByteArray(image_db, 0, image_db.length);
            profile.setImageBitmap(bmp);
        }

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
                        profile.setImageBitmap(selectedImageBitmap);
                    }
                }
            });

}