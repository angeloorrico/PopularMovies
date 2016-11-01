package br.com.angeloorrico.popularmovies.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MovieResponseModel implements Serializable {

    private ArrayList<MovieModel> results;

    public ArrayList<MovieModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieModel> results) {
        this.results = results;
    }

}