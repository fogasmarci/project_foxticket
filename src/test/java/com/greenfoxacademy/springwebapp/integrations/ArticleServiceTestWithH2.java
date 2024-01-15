package com.greenfoxacademy.springwebapp.integrations;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ArticleServiceTestWithH2 {

  ArticleService articleService;

  @Autowired
  public ArticleServiceTestWithH2(ArticleService articleService) {
    this.articleService = articleService;
  }

  @Test
  void listArticles_WithNullParam_ReturnsArticleListDtoWithAllArticles() {
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    article1.setId(1L);

    Article article2 = new Article("Test Title", "Test Content");
    article2.setPublishDate(LocalDate.of(2023, 12, 11));
    article2.setId(2L);

    List<Article> articles = new ArrayList<>();
    articles.add(article1);
    articles.add(article2);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    List<Article> actualArticlesToList = new ArrayList<>();
    actualArticlesToList.add(articleService.listArticles(null).getArticles().get(0));
    actualArticlesToList.add(articleService.listArticles(null).getArticles().get(1));

    assertThat(actualArticlesToList).usingRecursiveComparison().isEqualTo(articleListDTO.getArticles());
  }

  @Test
  void listArticles_WithStringParam_ReturnsArticleListDtoWithFilteredArticles() {
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    article1.setId(1L);

    List<Article> articles = new ArrayList<>();
    articles.add(article1);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    assertThat(articleService.listArticles("News about tickets").getArticles().get(0)).usingRecursiveComparison().isEqualTo(articleListDTO.getArticles().get(0));
  }
}
