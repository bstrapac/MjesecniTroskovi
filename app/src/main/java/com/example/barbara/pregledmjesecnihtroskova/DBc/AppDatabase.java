package com.example.barbara.pregledmjesecnihtroskova.DBc;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.barbara.pregledmjesecnihtroskova.DAO.ZapisDAO;
import com.example.barbara.pregledmjesecnihtroskova.Model.Zapis;


@Database(entities = {Zapis.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract ZapisDAO getZapisiDAO();

}
