package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.models.Article;

import java.util.List;

public interface ArticleService {

    ArticleListDTO listAllArticles();
}
