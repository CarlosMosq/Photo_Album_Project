package com.company.photoalbumproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Photo.class}, version = 2)
public abstract class PhotoDatabase extends RoomDatabase {

    private static PhotoDatabase instance;

    public abstract PhotoDao photoDao();

    public static synchronized PhotoDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room
                    .databaseBuilder(
                        context.getApplicationContext(),
                        PhotoDatabase.class,
                        "photo_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    // Code added in case I needed to populate the Db with previously existing data

//    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//        }
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private final PhotoDao photoDao;
//
//        private PopulateDbAsyncTask(PhotoDatabase database) {
//            photoDao = database.photoDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            return null;
//        }
//    }

}
