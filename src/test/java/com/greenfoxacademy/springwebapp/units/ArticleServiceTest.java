package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import com.greenfoxacademy.springwebapp.services.ArticleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class ArticleServiceTest {
  ArticleRepository articleRepository;
  ArticleServiceImpl articleService;

  public ArticleServiceTest() {
    articleRepository = Mockito.mock(ArticleRepository.class);
    articleService = new ArticleServiceImpl(articleRepository);
  }

  @Test
  void listArticles_WithNullParam_ReturnsArticleListDtoWithAllArticles() {
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    Article article2 = new Article("Test Title", "Test Content");
    article2.setPublishDate(LocalDate.of(2023, 12, 11));

    List<Article> articles = new ArrayList<>();
    articles.add(article1);
    articles.add(article2);

    Mockito.when(articleRepository.findAll()).thenReturn(articles);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    assertEquals(articleListDTO.getArticles(), articleService.listArticles(null).getArticles());
  }

  @Test
  void listArticles_WithStringParam_ReturnsArticleListDtoWithFilteredArticles() {
    List<Article> articles = new ArrayList<>();
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    articles.add(article1);

    Mockito.when(articleRepository.findArticlesByKeyword("News about tickets")).thenReturn(articles);
    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    assertEquals(articleListDTO.getArticles(), articleService.listArticles("News about tickets").getArticles());
  }
}
