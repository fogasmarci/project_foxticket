package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.AddArticleDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.exceptions.article.ContentRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.article.TitleAlreadyExistsException;
import com.greenfoxacademy.springwebapp.exceptions.article.TitleRequiredException;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
  private final ArticleRepository articleRepository;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  @Override
  public ArticleListDTO listArticles(String keyword) {
    ArticleListDTO articleListDTO = new ArticleListDTO();
    if (keyword == null) {
      articleListDTO.setArticles(articleRepository.findAll());
      return articleListDTO;
    }

    articleListDTO.setArticles(articleRepository.findArticlesByKeyword(keyword));
    return articleListDTO;
  }

  @Override
  public Article addArticle(AddArticleDTO addArticleDTO) {
    if (addArticleDTO.getTitle() == null) {
      throw new TitleRequiredException();
    }
    if (addArticleDTO.getContent() == null) {
      throw new ContentRequiredException();
    }

    Article existingArticle = articleRepository.findByTitle(addArticleDTO.getTitle()).orElse(null);
    if (existingArticle != null) {
      throw new TitleAlreadyExistsException();
    }

    return articleRepository.save(new Article(addArticleDTO.getTitle(), addArticleDTO.getContent()));
  }
}
