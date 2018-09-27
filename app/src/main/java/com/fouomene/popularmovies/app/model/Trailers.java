package com.fouomene.popularmovies.app.model;

import java.io.Serializable;
import java.util.List;

public class Trailers implements Serializable {

    private Long id;
    private List<Trailer> results = null;
    private final static long serialVersionUID = 8540512127050409877L;

    public Trailers() {
    }

    public Trailers(Long id, List<Trailer> results) {
        this.id = id;
        this.results = results;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<Trailer> getResults() {
        return results;
    }
    public void setResults(List<Trailer> results) {
        this.results = results;
    }

}