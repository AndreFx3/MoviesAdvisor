package andrewpolvoko.moviesadvisor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements
        RealmRecyclerView.OnLoadMoreListener,
        RealmRecyclerView.OnRefreshListener {

    private Realm realm;
    private RealmRecyclerView realmRecyclerView;
    private RealmResults<MovieRealmEntity> movieList;
    private boolean isGridEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.in_theaters);

        realmRecyclerView = (RealmRecyclerView) findViewById(R.id.realm_recycler_view);
        realm = Realm.getDefaultInstance();


        movieList = realm.where(MovieRealmEntity.class).findAllSorted("releaseDate", Sort.DESCENDING);
        RecyclerViewLinearAdapter adapter = new RecyclerViewLinearAdapter(this, movieList, true, false);
        realmRecyclerView.setAdapter(adapter);
        realmRecyclerView.setOnLoadMoreListener(this);
        realmRecyclerView.setOnRefreshListener(this);
        if (realmRecyclerView.getRecycleView().getAdapter().getItemCount() == 0)
            loadNextPage();
        //realmRecyclerView.enableShowLoadMore();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onLoadMore(Object o) {
        loadNextPage();
    }

    @Override
    public void onRefresh() {
        downloadResultsPage(1);
    }

    public void loadNextPage() {
        realmRecyclerView.setRefreshing(true);
        int nextPage = (realmRecyclerView.getRecycleView().getAdapter().getItemCount() / 20) + 1;
        downloadResultsPage(nextPage);
    }

    public void downloadResultsPage(final int page) {
        new Thread(new Runnable() {
            public void run() {

                while (MyApplication.mTmdbApi == null)
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                TmdbMovies movies = MyApplication.mTmdbApi.getMovies();
                List<MovieDb> moviesList = movies.getNowPlayingMovies("en", page).getResults();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (MovieDb movie : moviesList) {
                    realm.copyToRealmOrUpdate(new MovieRealmEntity(movie));
                }
                realm.commitTransaction();
                realm.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        realmRecyclerView.setRefreshing(false);
                        realmRecyclerView.enableShowLoadMore();
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change_layout) {
            if (isGridEnabled) {
                realmRecyclerView.getRecycleView().setLayoutManager(new LinearLayoutManager(this));
                realmRecyclerView.setAdapter(new RecyclerViewLinearAdapter(this, movieList, true, false));
                item.setIcon(R.drawable.ic_grid_layout_white_24dp);
                isGridEnabled = false;

            } else {
                realmRecyclerView.getRecycleView().setLayoutManager(new GridLayoutManager(this, 3));
                realmRecyclerView.setAdapter(new RecyclerViewGridAdapter(this, movieList, true, false));
                item.setIcon(R.drawable.ic_list_layout_white_24dp);
                isGridEnabled = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        realm = null;
    }
}