package com.example.simonor.metadataapp;

import android.os.Parcel;
import android.os.Parcelable;


public class Picture implements Parcelable
{
    String Name;
    String Url;
    String Keywords;
    String Date;
    Boolean Share;
    String Email;
    int Star;
    int resID;

    @Override
    public int describeContents() {
        return 0;
    }

    //storing data to parcel object
    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(Name);
        parcel.writeString(Url);
        parcel.writeString(Keywords);
        parcel.writeString(Date);
        parcel.writeInt(Share ? 1 : 0);
        parcel.writeString(Email);
        parcel.writeInt(Star);
        parcel.writeInt(resID);
    }
    //constructor
    public Picture(String name, String email, int resid)
    {
        this.Name = name;
        this.Email = email;
        this.resID = resid;
        this.Share = false;
    }

    //retrieving data from parcel
    private Picture(Parcel in)
    {
        Name = in.readString();
        Url = in.readString();
        Keywords = in.readString();
        Date = in.readString();
        Share = in.readInt() != 0;
        Email = in.readString();
        Star = in.readInt();
        resID = in.readInt();
    }

    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>(){
        public Picture createFromParcel(Parcel in){
            return new Picture(in);
        }

        public Picture[] newArray(int size){
            return new Picture[size];
        }
    };

}
