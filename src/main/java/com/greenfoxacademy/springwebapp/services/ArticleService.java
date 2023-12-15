package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;

public interface ArticleService {

  ArticleListDTO listArticles(String keyword);
}
