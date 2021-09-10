package com.company.photoalbumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhotoViewModel photoViewModel;
    public FloatingActionButton addPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PhotoAdapter photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        photoViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(PhotoViewModel.class);
        photoViewModel.getAllPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                photoAdapter.setPhotos(photos);
            }
        });


        addPhoto = findViewById(R.id.addPicture);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageUploader.class);
                startActivityForResult(intent, 3);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0
                , ItemTouchHelper.RIGHT
                | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView
                    , @NonNull RecyclerView.ViewHolder viewHolder
                    , @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                photoViewModel.delete(photoAdapter.getPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Image deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        photoAdapter.setListener(new PhotoAdapter.onImageClickListener() {
            @Override
            public void onImageClick(Photo photo) {
                Intent intent = new Intent(MainActivity.this, ImageUpdate.class);
                intent.putExtra("id", photo.getId());
                intent.putExtra("title", photo.getTitle());
                intent.putExtra("description", photo.getDescription());
                intent.putExtra("image", photo.getPhotoItem());
                startActivityForResult(intent, 4);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK) {
            byte[] image = data.getByteArrayExtra("imageFile");
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");

            Photo photo = new Photo(image, title, description);
            photoViewModel.insert(photo);
        }

        else if (requestCode == 4 && resultCode == RESULT_OK) {
            byte[] updateImage = data.getByteArrayExtra("updateImageFile");
            String updateTitle = data.getStringExtra("updateTitle");
            String updateDescription = data.getStringExtra("updateDescription");
            int id = data.getIntExtra("id", -1);

            Photo photo = new Photo(updateImage, updateTitle, updateDescription);
            photo.setId(id);
            photoViewModel.update(photo);
        }

    }
}