package com.example.finalprojectvirtualteacher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WikiPage {
    @JsonProperty("title")
    private String title;
    @JsonProperty("contentSnippet")
    private String contentSnippet;
    @JsonProperty("fullUrl")
    private String fullUrl;

    public WikiPage() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentSnippet() {
        return contentSnippet;
    }

    public void setContentSnippet(String contentSnippet) {
        this.contentSnippet = contentSnippet;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }
}