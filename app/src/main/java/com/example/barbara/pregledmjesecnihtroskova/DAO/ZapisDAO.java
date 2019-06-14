package com.example.barbara.pregledmjesecnihtroskova.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.barbara.pregledmjesecnihtroskova.Model.Zapis;
import java.util.List;

@Dao
public interface ZapisDAO
{
    @Query("SELECT * FROM Zapis ORDER BY date")
     List<Zapis> getZapisi();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void AddNew(Zapis zapis);

    @Delete
    void Delete(Zapis zapis);

    @Query("SELECT * FROM Zapis WHERE id = :id LIMIT 1")
    Zapis findZapisId(int id);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Zapis zapis);

    @Query("SELECT * FROM Zapis WHERE date BETWEEN :startofmonth AND :endofmonth")
    List<Zapis> getZapisiByDate(long startofmonth, long endofmonth);
}
