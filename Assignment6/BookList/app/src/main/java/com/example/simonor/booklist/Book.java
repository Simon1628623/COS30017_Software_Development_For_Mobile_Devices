package com.example.simonor.booklist;

import android.graphics.Picture;

/**
 * Created by Simonor on 22/09/2016.
 */

public class Book
{
    public String name;
    public Integer rating;
    public Picture icon;

    public Book()
    {
        name = "empty";
        rating = 5;

    }

    public Book(String n, Integer r, Picture p)
    {
        name = n;
        rating = r;
        icon = p;
    }

    public Book(String n, Integer r)
    {
        name = n;
        rating = r;
    }
}


