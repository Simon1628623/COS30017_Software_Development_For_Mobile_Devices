package com.example.simonor.booklist;

/**
 * Created by Simonor on 22/09/2016.
 */

public class Books
{
    Book[] books;

    public Books()
    {
        Book[] result = new Book[10];

        for(int i = 0; i < 9; i++)
        {
            result[i] = new Book();
        }
    }

    public Book[] getBooks()
    {
        return books;
    }
}
