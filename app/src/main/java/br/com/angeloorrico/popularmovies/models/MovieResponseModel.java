package br.com.angeloorrico.popularmovies.models;

import java.util.List;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MovieResponseModel {

    private List<MovieModel> results;

    public List<MovieModel> getResults() {
        return results;
    }

    public void setResults(List<MovieModel> results) {
        this.results = results;
    }
}
