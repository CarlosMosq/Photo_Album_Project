package com.company.photoalbumproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUpdate extends AppCompatActivity {

    ImageView imageToUpdate;
    EditText titleToUpdate;
    EditText descriptionToUpdate;
    Button cancelUpdate;
    Button saveUpdate;
    String title, description;
    byte[] image;
    int id;
    Bitmap bitmapFile;
    Bitmap scaledFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Image");
        setContentView(R.layout.activity_image_update);

        imageToUpdate = findViewById(R.id.clickToUpdate);
        titleToUpdate = findViewById(R.id.update_title);
        descriptionToUpdate = findViewById(R.id.updateDescription);
        cancelUpdate = findViewById(R.id.cancelUpdate);
        saveUpdate = findViewById(R.id.saveUpdate);

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        image = getIntent().getByteArrayExtra("image");

        titleToUpdate.setText(title);
        descriptionToUpdate.setText(description);
        imageToUpdate.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));


        cancelUpdate.setOnClickListener(v -> {
            Toast.makeText(ImageUpdate.this, "Nothing saved", Toast.LENGTH_SHORT).show();
            finish();
        });
        
        saveUpdate.setOnClickListener(v -> {
            updateImage();
        });

        imageToUpdate.setOnClickListener(v -> {
            startActivityForResult(
                    new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    , 5);
        });

    }

    public void updateImage() {
        if (image == null) {
            Toast.makeText(ImageUpdate.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
        else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapFile = BitmapFactory.decodeByteArray(image, 0, image.length);
            scaledFile = makeImageSmaller(bitmapFile, 300);
            scaledFile.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String title = titleToUpdate.getText().toString();
            String description = descriptionToUpdate.getText().toString();
            Intent i = new Intent();
            i.putExtra("id", id);
            i.putExtra("updateImageFile", byteArray);
            i.putExtra("updateTitle", title);
            i.putExtra("updateDescription", description);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    public Bitmap makeImageSmaller(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;

        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        }
        else {
            height = maxSize;
            width = (int) (height * ratio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 5 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageToUpdate.setImageBitmap(bitmap);
                bitmapFile = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
