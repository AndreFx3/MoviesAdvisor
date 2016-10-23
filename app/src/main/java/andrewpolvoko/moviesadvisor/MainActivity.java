package andrewpolvoko.moviesadvisor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.redmadrobot.chronos.ChronosConnector;

import java.net.URL;

import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.MovieDb;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ChronosConnector mConnector = new ChronosConnector();
    private TextView titleTV;
    private TextView overviewTV;
    private ImageView IVBackdrop;
    private ImageView IVPoster;
    private MovieOperation mMovieOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnector.onCreate(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleTV = (TextView) findViewById(R.id.titleTV);
        overviewTV = (TextView) findViewById(R.id.overviewTV);
        IVBackdrop = (ImageView) findViewById(R.id.IVBackdrop);
        IVPoster = (ImageView) findViewById(R.id.IVPoster);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mMovieOperation = new MovieOperation();
        mConnector.runOperation(mMovieOperation , false);
    }

    public void onOperationFinished(final MovieOperation.Result result) {
        if (result.isSuccessful()) {
            showData(result.getOutput());
        } else {
            showDataLoadError(result.getException());
        }
    }

    private void showData(MovieDb movie){
        titleTV.setText(movie.getOriginalTitle());
        overviewTV.setText(movie.getOverview());
        URL PosterImageUrl = Utils.createImageUrl(mMovieOperation.mTmdbApi, movie.getPosterPath(), "w500");
        Glide.with(this).load(PosterImageUrl.toString()).into(IVPoster);
        URL BackdropImageUrl = Utils.createImageUrl(mMovieOperation.mTmdbApi, movie.getBackdropPath(), "w500");
        Glide.with(this).load(BackdropImageUrl.toString()).into(IVBackdrop);
    }

    private void showDataLoadError(Exception exception){
        /*StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        sw.toString();*/
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnector.onPause();
    }
}
