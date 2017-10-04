package com.veronika.android.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class defines the Movie as object with fields
 */

public class AndroidMovie implements Parcelable{

    private String mTitleMovie;
    private String mOverview;
    private String mMovieImage;
    private Double mVoteAverage;
    private String mReleaseDate;
    private int mId;



    /**
     * Constructor
     */
    public AndroidMovie(){

    }

    private AndroidMovie(Parcel in){

        mTitleMovie = in.readString();
        mOverview= in.readString();
        mMovieImage = in.readString();
        mVoteAverage = in.readDouble();
        mReleaseDate = in.readString();
      //  mId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return mMovieImage;
    }
    /*public String toString() {
        return titleMovie + "--" + description + "--" + imageMovie;
    }
    */

    @Override
    public void writeToParcel(Parcel parcel, int in) {
        parcel.writeString(mTitleMovie);
        parcel.writeString(mOverview);
        parcel.writeString(mMovieImage);
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mReleaseDate);
      //  parcel.writeInt(mId);

    }
    public static final Parcelable.Creator<AndroidMovie> CREATOR = new Parcelable.Creator<AndroidMovie>() {
        @Override
        public AndroidMovie createFromParcel(Parcel parcel) {
            return new AndroidMovie(parcel);
        }

        @Override
        public AndroidMovie[] newArray(int size) {
            return new AndroidMovie[size];
        }

    };

    public void setTitle(String titleMovie){
        mTitleMovie = titleMovie;
    }

    public void setOverview(String overview){
        mOverview = overview;
    }
    public void setImage(String image){
        mMovieImage = image;
    }

    public void setAverage(Double voteAverage){
        mVoteAverage = voteAverage;
    }
    public void setReleaseDate(String releaseDate){
        mReleaseDate = releaseDate;
    }
    public void setId(int id){
        mId = id;
    }

    public String getTitle(){
        return mTitleMovie;
    }
    public String getOverview(){
        return mOverview;
    }
    public String getMovieImage(){
        return mMovieImage;
    }
    public Double getVoteAverage(){
        return mVoteAverage;
    }
    public String getReleaseDate(){
        return mReleaseDate;
    }
   // public int getId(){return mId;}

}
