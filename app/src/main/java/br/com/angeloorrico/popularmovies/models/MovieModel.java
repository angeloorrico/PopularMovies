package br.com.angeloorrico.popularmovies.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.angeloorrico.popularmovies.utils.Utils;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MovieModel implements Serializable {

    private String title;

    private Date release_date;

    private String poster_path;

    private String backdrop_path;

    private String vote_average;

    private String overview;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        SimpleDateFormat sdf;
        if (Utils.getDeviceLocale().equals("pt-BR"))
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        else
            sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(release_date);
    }

    public void setReleaseDate(Date releaseDate) {
        this.release_date = releaseDate;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster) {
        poster_path = poster;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(String voteAverage) {
        this.vote_average = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdrop_path = backdropPath;
    }

}
