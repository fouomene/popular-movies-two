package com.fouomene.popularmovies.app.model;

import java.io.Serializable;

public class Review implements Serializable {

    private String author;
    private String content;
    private String id;
    private String url;
    private final static long serialVersionUID = 4257597061476868437L;

    public Review() {
    }

    public Review(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}