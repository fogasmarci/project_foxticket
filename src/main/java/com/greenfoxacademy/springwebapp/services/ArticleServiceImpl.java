package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.AddArticleDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.article.ArticleNotExistsException;
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
    validateAddArticleDTO(addArticleDTO);

    if (articleRepository.existsByTitle(addArticleDTO.title())) {
      throw new TitleAlreadyExistsException();
    }

    return articleRepository.save(mapDTOToArticle(addArticleDTO));
  }

  @Override
  public Article editArticle(AddArticleDTO addArticleDTO, Long articleId) {
    validateAddArticleDTO(addArticleDTO);

    Article articleToEdit = articleRepository.findById(articleId)
        .orElseThrow(ArticleNotExistsException::new);

    Article existingArticle = articleRepository.findByTitle(addArticleDTO.title()).orElse(null);
    if (existingArticle != null && !existingArticle.getTitle().equals(articleToEdit.getTitle())) {
      throw new TitleAlreadyExistsException();
    }

    articleToEdit.setTitle(addArticleDTO.title());
    articleToEdit.setContent(addArticleDTO.content());

    return articleRepository.save(articleToEdit);
  }

  @Override
  public MessageDTO deleteArticle(Long articleId) {
    Article articleToDelete = articleRepository.findById(articleId)
        .orElseThrow(ArticleNotExistsException::new);

    articleRepository.delete(articleToDelete);
    String okMessage = String.format("Article %d is deleted.", articleId);

    return new MessageDTO(okMessage);
  }

  public Article mapDTOToArticle(AddArticleDTO addArticleDTO) {
    return new Article(addArticleDTO.title(), addArticleDTO.content());
  }

  private void validateAddArticleDTO(AddArticleDTO addArticleDTO) {
    if (addArticleDTO.title() == null) {
      throw new TitleRequiredException();
    }
    if (addArticleDTO.content() == null) {
      throw new ContentRequiredException();
    }
  }
}
