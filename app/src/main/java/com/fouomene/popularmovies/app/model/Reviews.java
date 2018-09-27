package com.fouomene.popularmovies.app.model;

import java.io.Serializable;
import java.util.List;

public class Reviews implements Serializable {

    private Long id;
    private Long page;
    private List<Review> results = null;
    private Long totalPages;
    private Long totalResults;
    private final static long serialVersionUID = 9091973750735908869L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

}
