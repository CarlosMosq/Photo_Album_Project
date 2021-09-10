package com.company.photoalbumproject;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoRepository {
    private final PhotoDao photoDao;
    private final LiveData<List<Photo>> photos;

    public PhotoRepository(Application application) {
        PhotoDatabase photoDatabase = PhotoDatabase.getInstance(application);
        photoDao = photoDatabase.photoDao();
        photos = photoDao.getAllPhotos();
    }

    public void insert(Photo photo){
        new InsertPhotoAsyncTask(photoDao).execute(photo);
    }

    public void update(Photo photo){
        new UpdatePhotoAsyncTask(photoDao).execute(photo);
    }

    public void delete(Photo photo){
        new DeletePhotoAsyncTask(photoDao).execute(photo);
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return photos;
    }

    private static class InsertPhotoAsyncTask extends AsyncTask<Photo, Void, Void>{
        private final PhotoDao photoDao;

        private InsertPhotoAsyncTask(PhotoDao photoDao){
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.Insert(photos[0]);
            return null;
        }
    }

    private static class UpdatePhotoAsyncTask extends AsyncTask<Photo, Void, Void>{
        private final PhotoDao photoDao;

        private UpdatePhotoAsyncTask(PhotoDao photoDao){
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.Update(photos[0]);
            return null;
        }
    }

    private static class DeletePhotoAsyncTask extends AsyncTask<Photo, Void, Void>{
        private final PhotoDao photoDao;

        private DeletePhotoAsyncTask(PhotoDao photoDao){
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.Delete(photos[0]);
            return null;
        }
    }

}
