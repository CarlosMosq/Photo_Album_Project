package com.company.photoalbumproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUploader extends AppCompatActivity {

    ImageView imageToUpload;
    EditText itemTitle;
    EditText itemDescription;
    Button cancel;
    Button save;
    Bitmap imageFile;
    Bitmap scaledFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Upload Image");
        setContentView(R.layout.activity_image_uploader);

        imageToUpload = findViewById(R.id.clickToUpdate);
        itemTitle = findViewById(R.id.update_title);
        itemDescription = findViewById(R.id.updateDescription);
        cancel = findViewById(R.id.cancelUpdate);
        save = findViewById(R.id.saveUpdate);

        imageToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(ImageUploader.this
                        , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(
                                    ImageUploader.this
                                    , new String [] {Manifest.permission.READ_EXTERNAL_STORAGE}
                                    , 1);
                }
                else {
                    startActivityForResult(
                            new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                            , 2);
                }



            }
        });

        save.setOnClickListener(v -> saveImage());

        cancel.setOnClickListener(v -> {
            Toast.makeText(ImageUploader.this, "Nothing saved", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    public void saveImage() {
        if (imageFile == null) {
            Toast.makeText(ImageUploader.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
        else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledFile = makeImageSmaller(imageFile, 300);
            scaledFile.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String title = itemTitle.getText().toString();
            String description = itemDescription.getText().toString();
            Intent i = new Intent();
            i.putExtra("imageFile", byteArray);
            i.putExtra("title", title);
            i.putExtra("description", description);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageToUpload.setImageBitmap(bitmap);
                imageFile = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}