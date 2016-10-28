package andrewpolvoko.moviesadvisor;

import com.fasterxml.jackson.annotation.JsonProperty;

import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class MovieRealmEntity extends RealmObject {
    public MovieRealmEntity() {
        super();
    }

    public MovieRealmEntity(MovieDb movie) {
        super();
        title = movie.getTitle();
        originalTitle = movie.getOriginalTitle();
        popularity = movie.getPopularity();
        backdropPath = movie.getBackdropPath();
        fullbackdropPath = String.valueOf(Utils.createImageUrl(MyApplication.mTmdbApi, backdropPath, "w1280"));
        posterPath = movie.getPosterPath();
        fullPosterPath = String.valueOf(Utils.createImageUrl(MyApplication.mTmdbApi, posterPath, "w342"));
        releaseDate = movie.getReleaseDate();
        adult = movie.isAdult();
        budget = movie.getBudget();
        homepage = movie.getHomepage();
        overview = movie.getOverview();
        Id = movie.getId();
        imdbID = movie.getImdbID();
        revenue = movie.getRevenue();
        runtime = movie.getRuntime();
        tagline = movie.getTagline();
        userRating = movie.getUserRating();
        voteAverage = movie.getVoteAverage();
        voteCount = movie.getVoteCount();
        status = movie.getStatus();
    }

    @PrimaryKey
    @JsonProperty("title")
    private String title;
    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("popularity")
    private float popularity;

    @JsonProperty("backdrop_path")
    private String backdropPath;
    private String fullbackdropPath;
    @JsonProperty("poster_path")
    private String posterPath;
    private String fullPosterPath;

    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("budget")
    private long budget;
    /*@JsonProperty("genres")
    private RealmList<String> genres;*/
    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("id")
    @Index
    private int Id;

    @JsonProperty("imdb_id")
    @Index
    private String imdbID;

    @JsonProperty("revenue")
    private long revenue;
    @JsonProperty("runtime")
    private int runtime;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("rating")
    private float userRating;

    @JsonProperty("vote_average")
    private float voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;

    @JsonProperty("status")
    private String status;

    // Appendable responses

    /*@JsonProperty("keywords")
    private RealmList<String> keywords;*/


    public String getBackdropPath() {
        return backdropPath;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }


    public float getPopularity() {
        return popularity;
    }


    public String getPosterPath() {
        return posterPath;
    }


    public String getReleaseDate() {
        return releaseDate;
    }


    public String getTitle() {
        return title;
    }


    public boolean isAdult() {
        return adult;
    }


    public long getBudget() {
        return budget;
    }


    /*public RealmList<String> getGenres() {
        return genres;
    }*/


    public String getHomepage() {
        return homepage;
    }


    public String getImdbID() {
        return imdbID;
    }

    public int getId() {
        return Id;
    }


    public String getOverview() {
        return overview;
    }


    public long getRevenue() {
        return revenue;
    }


    public int getRuntime() {
        return runtime;
    }


    public String getTagline() {
        return tagline;
    }


    public float getVoteAverage() {
        return voteAverage;
    }


    public int getVoteCount() {
        return voteCount;
    }


    public String getStatus() {
        return status;
    }


    /*public RealmList<String> getKeywords() {
        return keywords;
    }*/

    public String getFullbackdropPath() {
        return fullbackdropPath;
    }

    public String getFullPosterPath() {
        return fullPosterPath;
    }

    public float getUserRating() {
        return userRating;
    }


    @Override
    public String toString() {
        return title + " - " + releaseDate;
    }

    public Multi.MediaType getMediaType() {
        return Multi.MediaType.MOVIE;
    }
}
