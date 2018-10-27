package ajikamaludin.id.mybioskop;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    public final static String MOVIE = "MOVIE";

    private RecyclerView recyclerView;
    private ArrayList<Movie> list;
    private  MenuItem mSearch;
    private ProgressBar progressBar;

    private String URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=67b6380268ec08352e29f665e862f113&region=ID";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), R.string.action_settings ,Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.list_activity_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.mainRecycler);
        recyclerView.setHasFixedSize(true);

        if(this.isOnline() || list != null){
            LoaderManager lm = getLoaderManager();
            lm.initLoader(0, null, this).forceLoad();
        }else{
            setContentView(R.layout.offline);
        }

    }

    private void showRecyclerCardView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CardViewMovieAdapter CardViewMovieAdapter = new CardViewMovieAdapter(this);
        CardViewMovieAdapter.setMovies(list);
        recyclerView.setAdapter(CardViewMovieAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                startIntent(position);
            }
        });
    }

    private void startIntent(int positon){
        Intent intent = new Intent(MainActivity.this, DetailMovie.class);
        intent.putExtra(MOVIE, list.get(positon));
        startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieDataAsync(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        this.list = movies;
        if(this.list != null){
            showRecyclerCardView();
        }else{
            Toast.makeText(getApplicationContext(), "Upps Something Go Wrong",Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        this.list = null;
    }
}
