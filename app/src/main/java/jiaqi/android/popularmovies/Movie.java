package jiaqi.android.popularmovies;

import java.io.Serializable;

/**
 * Created by Jiaqi on 5/3/2016.
 */
public class Movie implements Serializable{
    private String mName;
    private String mPath;
    private String mFullPath;

    public Movie(String name, String path) {
        mName = name;
        mPath = path;
        mFullPath = TMDbFetcher.POSTER_BASE_URL_STRING + "original" + path;
    }

    public String getFullPath() {
        return mFullPath;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

}
