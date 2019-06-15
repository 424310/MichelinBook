package com.example.lasttest;

public class Post {
    public String post_date;
    public String post_content;
    public String post_image;

    public Post() {

    }

    public Post(String post_date, String post_content, String post_image) {
        this.post_date = post_date;
        this.post_content = post_content;
        this.post_image = post_image;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }
}
