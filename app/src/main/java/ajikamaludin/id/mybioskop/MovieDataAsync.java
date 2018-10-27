package ajikamaludin.id.mybioskop;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aji on 10/27/18.
 */

public class MovieDataAsync extends AsyncTaskLoader<ArrayList<Movie>> {

    private String URL;
    private String movies = null;

    public MovieDataAsync(Context context, String URL) {
        super(context);
        this.URL = URL;
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        final ArrayList<Movie> list = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        try {
            Response response = client.newCall(request).execute();

            movies = response.body().string();
            try{
                JSONObject objData = new JSONObject(movies);
                final JSONArray arrayResults = objData.getJSONArray("results");
                if(arrayResults != null) {
                    for (int i = 0; i < arrayResults.length(); i++) {
                        JSONObject objMovie = new JSONObject(arrayResults.get(i).toString());
                        String title = objMovie.getString("title");
                        String overview = objMovie.getString("overview");
                        String releaseDate = objMovie.getString("release_date");
                        String imgPoster = "http://image.tmdb.org/t/p/w185" + objMovie.getString("poster_path");
                        list.add(new Movie(title, overview, releaseDate, imgPoster));
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
