package andrewpolvoko.moviesadvisor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import io.realm.Realm;


class MovieOperation extends ChronosOperation<MovieDb> {

    @Nullable
    @Override
    public MovieDb run() {
        final MovieDb result = null;
        if (MyApplication.mTmdbApi == null)
            MyApplication.mTmdbApi = new TmdbApi(Constants.TMDB_API_KEY);

        TmdbMovies movies = MyApplication.mTmdbApi.getMovies();
        List<MovieDb> moviesList;
        moviesList = movies.getNowPlayingMovies("en", 1).getResults();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (MovieDb movie : moviesList) {
            realm.copyToRealmOrUpdate(new MovieRealmEntity(movie));
        }

        realm.commitTransaction();
        realm.close();
        return result;
    }

    @NonNull
    @Override
    // To be able to distinguish results from different operations in one Chronos client
    // (most commonly an activity, or a fragment)
    // you should create an 'OperationResult<>' subclass in each operation,
    // so that it will be used as a parameter
    // in a callback method 'onOperationFinished'
    public Class<? extends ChronosOperationResult<MovieDb>> getResultClass() {
        return Result.class;
    }

    // the class is a named version of ChronosOperationResult<> generic class
    // it is required because Java disallows method overriding by using generic class with another parameter
    // and result delivery is based on calling particular methods with the exact same result class
    // later we'll see how Chronos use this class
    public final static class Result extends ChronosOperationResult<MovieDb> {
        // usually this class is empty, but you may add some methods to customize its behavior
        // however, it must have a public constructor with no arguments
    }
}