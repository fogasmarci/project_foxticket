package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.time.LocalDate;

@Entity
@Table(name = "news")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    /*@Column(name = "publish_date")*/
    private LocalDate publishDate;

    public Article() {
        publishDate = LocalDate.now();
    }

    public Article(String title, String content) {
        this();
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }
}












