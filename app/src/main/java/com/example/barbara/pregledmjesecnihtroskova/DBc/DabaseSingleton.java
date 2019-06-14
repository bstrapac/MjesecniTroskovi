package com.example.barbara.pregledmjesecnihtroskova.DBc;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DabaseSingleton {
    private static final String DATABASE_NAME = "zapis-db";

    private static  DabaseSingleton sInstance;
    AppDatabase databaseInstance;

    private DabaseSingleton(Context context){
        databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();//spajanje na bazu
    }
    public static  DabaseSingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DabaseSingleton(context);
        }
        return sInstance;
    }

    public AppDatabase getDatabaseInstance() {
        return databaseInstance;
    }
}
