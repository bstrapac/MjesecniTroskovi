package com.example.barbara.pregledmjesecnihtroskova.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Zapis
{
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int Id;
    @ColumnInfo(name = "date")
    public Long CreatedDate;
    @ColumnInfo(name = "input")
    public int Input;
    @ColumnInfo(name = "description")
    public String Description;

    public int getId()
    {
        return Id;
    }

    public void setId( int id) {
        Id = id;
    }

    public Long getCreatedDate()
    {
        return CreatedDate;
    }

    public void setCreatedDate(Long createdDate) {
        CreatedDate = createdDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getInput() {
        return Input;
    }

    public void setInput(int input) {
        Input = input;
    }
}
