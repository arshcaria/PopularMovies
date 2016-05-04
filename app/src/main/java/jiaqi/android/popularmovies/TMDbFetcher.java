package jiaqi.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jiaqi on 5/3/2016.
 */
public class TMDbFetcher {

    public static final String API_KEY_STRING = "?api_key=6e43a8122cec30c9f4ed103b92e0a702";
    public static final String TOP_RATED_URL_STRING = "http://api.themoviedb.org/3/movie/top_rated";
    public static final String MOST_POPULAR_URL_STRING = "http://api.themoviedb.org/3/movie/popular";

    public static final String POSTER_BASE_URL_STRING = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE_W185 = "w185";

    public enum MovieListType {
        TOP_RATED, MOST_POPULAR
    }

    private byte[] getUrlResultBytes(String urlString) {
        byte[] urlResultBytes = new byte[0];
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlString);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();

            connection.disconnect();

            urlResultBytes = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlResultBytes;
    }

    private String getUrlResultString(String urlString) {
        return new String(getUrlResultBytes(urlString));
    }

    public ArrayList<Movie> getMostPopularList() {
        return getMovieList(MovieListType.MOST_POPULAR);
    }

    public ArrayList<Movie> getTopRatedList() {
        return getMovieList(MovieListType.TOP_RATED);
    }

    private ArrayList<Movie> getMovieList(MovieListType type) {
        ArrayList<Movie> topRatedList = new ArrayList<>();
        String jsonString = "";
        switch (type) {
            case TOP_RATED:
                jsonString = getUrlResultString(TOP_RATED_URL_STRING + API_KEY_STRING);
                break;
            case MOST_POPULAR:
                jsonString = getUrlResultString(MOST_POPULAR_URL_STRING + API_KEY_STRING);
                break;
        }

        try {
            JSONObject jsonBody = new JSONObject(jsonString);
            JSONArray jsonMovieList = jsonBody.getJSONArray("results");
            for (int i = 0; i < jsonMovieList.length(); i++) {
                String posterPath = jsonMovieList.getJSONObject(i).getString("poster_path");
                String movieName = jsonMovieList.getJSONObject(i).getString("title");
                topRatedList.add(new Movie(movieName, posterPath));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topRatedList;
    }
}
