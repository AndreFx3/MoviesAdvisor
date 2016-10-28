package andrewpolvoko.moviesadvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import io.realm.Realm;

public class MovieInfoActivity extends AppCompatActivity {
    private Realm realm;
    private ImageView backdropIV;
    private ImageView posterIV;
    private ProgressBar backdropPB;
    private ProgressBar posterPB;
    private TextView titleTV;
    private TextView overviewTV;
    private TextView Revenue;
    private TextView Budget;
    private TextView ReleaseDate;
    private TextView Runtime;
    private TextView VoteAverage;
    private int filmId;
    private MovieRealmEntity movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        realm = Realm.getDefaultInstance();

        backdropIV = (ImageView) findViewById(R.id.backdropIV);
        backdropPB = (ProgressBar) findViewById(R.id.backdropPB);
        posterIV = (ImageView) findViewById(R.id.posterIV);
        posterPB = (ProgressBar) findViewById(R.id.posterPB);
        titleTV = (TextView) findViewById(R.id.titleTV);
        overviewTV = (TextView) findViewById(R.id.overviewTV);
        Revenue = (TextView) findViewById(R.id.Revenue2);
        Budget = (TextView) findViewById(R.id.Budget2);
        ReleaseDate = (TextView) findViewById(R.id.ReleaseDate2);
        Runtime = (TextView) findViewById(R.id.Runtime2);
        VoteAverage = (TextView) findViewById(R.id.VoteAverage2);

        filmId = getIntent().getExtras().getInt("id");
        movie = realm.where(MovieRealmEntity.class).equalTo("Id", filmId).findFirst();

        titleTV.setText(isFieldEmpty(movie.getTitle()));
        overviewTV.setText(isFieldEmpty(movie.getOverview()));
        Revenue.setText(isFieldEmpty(movie.getRevenue()));
        Budget.setText(isFieldEmpty(movie.getBudget()));
        ReleaseDate.setText(isFieldEmpty(movie.getReleaseDate()));
        Runtime.setText(isFieldEmpty(movie.getRuntime()));
        VoteAverage.setText(isFieldEmpty(movie.getVoteAverage()));

        setPoster(movie.getFullPosterPath());
        setbackdrop(movie.getFullbackdropPath());
    }

    public String isFieldEmpty(String field) {
        if (field == null)
            return "N/A";
        else
            return field;
    }

    public String isFieldEmpty(int field) {
        if (field == 0)
            return "N/A";
        else
            return String.valueOf(field);
    }

    public String isFieldEmpty(long field) {
        if (field == 0)
            return "N/A";
        else
            return String.valueOf(field);
    }
    public String isFieldEmpty(float field) {
        if (field == 0)
            return "N/A";
        else
            return String.valueOf(field);
    }

    public void setPoster(String url) {
        if (!(url == null))
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            posterPB.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            posterPB.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(posterIV);
        else {
            posterIV.setImageResource(R.drawable.no_image);
        }
    }

    public void setbackdrop(String url) {
        if (!(url == null))
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            backdropPB.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            backdropPB.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(backdropIV);
        else {
            if (!(movie.getFullPosterPath() == null))
                Glide.with(this)
                        .load(movie.getFullPosterPath())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                backdropPB.setVisibility(View.INVISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                backdropPB.setVisibility(View.INVISIBLE);
                                return false;
                            }
                        })
                        .into(backdropIV);
            else
                posterIV.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        realm = null;
    }
}
