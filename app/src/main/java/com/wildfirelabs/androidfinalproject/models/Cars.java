package com.wildfirelabs.androidfinalproject.models;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cars_table", indices = {@Index(value="vin", unique=true)})
public class Cars implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String model;
    private int year;
    private float price;
    private String color;
    private String vin;

    @Nullable
    private byte[] image;

    public Cars(String name, String model, int year, float price, String color, String vin,@Nullable byte[] image)
    {
        this.name = name;
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color;
        this.vin = vin;
        this.image = image;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getModel()
    {
        return model;
    }

    public int getYear()
    {
        return year;
    }

    public float getPrice()
    {
        return price;
    }

    public String getColor()
    {
        return color;
    }

    public String getVin()
    {
        return vin;
    }

    public byte[] getImage()
    {
        return image;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public void setImage(@Nullable byte[] image)
    {
        this.image = image;
    }
}
