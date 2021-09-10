package com.company.photoalbumproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class Photo {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public byte[] photoItem;

    public String title;

    public String description;

    public Photo(byte[] photoItem, String title, String description) {
        this.photoItem = photoItem;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public byte[] getPhotoItem() {
        return photoItem;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }
}
