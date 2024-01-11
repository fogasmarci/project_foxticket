package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.AddArticleDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.models.Article;

public interface ArticleService {

  ArticleListDTO listArticles(String keyword);

  Article addArticle(AddArticleDTO addArticleDTO);

  Article editArticle(AddArticleDTO addArticleDTO, Long articleId);
}
