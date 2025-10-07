package com.isfar.quotesnotes;

import com.google.gson.annotations.SerializedName;

public class Quote {
    @SerializedName("quote")
    private String quote;
    @SerializedName("author")
    private String author;
    @SerializedName("category")
    private String category;

    public Quote(String quote, String author) {
        this.quote = quote;
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

}
