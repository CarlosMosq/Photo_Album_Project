package com.company.photoalbumproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;
import java.util.List;

@Dao
public interface PhotoDao {
    @Insert
    void Insert(Photo photo);
    @Update
    void Update(Photo photo);
    @Delete
    void Delete(Photo photo);

    @Query("SELECT * FROM photo_table ORDER BY id ASC")
    LiveData<List<Photo>> getAllPhotos();
}
