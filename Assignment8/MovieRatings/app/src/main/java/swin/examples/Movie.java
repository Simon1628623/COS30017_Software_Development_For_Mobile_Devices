package swin.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Movie
{
	private String name;
	private String rating;
	private String votes;
	
	public Movie(String pName, String pRating, String pVotes)
	{
		name = pName;
		rating = pRating;
		votes = pVotes;
	}
	
	/** Loads movie information from a raw resource file */
	public static ArrayList<Movie> loadFromFile(InputStream inputStream)
	{
		ArrayList<Movie> movies = new ArrayList<Movie>(20000);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		try
		{
			while ((line = br.readLine()) != null)
			{
				String lRatings = line.substring(0,3).trim();
				String lVotes = line.substring(4,12).trim();
				String lName = line.substring(13).trim();
				movies.add(new Movie(lName, lRatings, lVotes));
			}
		} catch (IOException iox) {} // pure evil at work
		return movies;
	}
	
	
	
	public String getName()
	{
		return name;
	}


	public String getRating()
	{
		return rating;
	}


	public String getVotes()
	{
		return votes;
	}
	
	
	
}
