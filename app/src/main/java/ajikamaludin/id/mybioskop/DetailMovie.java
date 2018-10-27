package ajikamaludin.id.mybioskop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailMovie extends AppCompatActivity {

    private ImageView imgPoster;
    private TextView txtTitle, txtOverview, txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Movie movie = (Movie) getIntent().getSerializableExtra(MainActivity.MOVIE);

        setTitle(R.string.detail_movie);

        imgPoster = (ImageView)findViewById(R.id.poster_movie);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        txtDate = (TextView)findViewById(R.id.txt_dateshow);
        txtOverview = (TextView)findViewById(R.id.txt_description);

        Glide.with(this)
                .load(movie.getImgPoster())
                .override(400, 350)
                .into(imgPoster);

        txtDate.setText(movie.getReleaseDate());
        txtTitle.setText(movie.getTxtTitle());
        txtOverview.setText(movie.getTxtOverview());
    }
}
