package com.company.photoalbumproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoViewModel extends AndroidViewModel {
    private final PhotoRepository repository;
    private final LiveData<List<Photo>> photos;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        repository = new PhotoRepository(application);
        photos = repository.getAllPhotos();
    }

    public void insert(Photo photo) {
        repository.insert(photo);
    }

    public void update(Photo photo) {
        repository.update(photo);
    }

    public void delete(Photo photo) {
        repository.delete(photo);
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return photos;
    }
}
