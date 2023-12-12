package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleServiceTestWithH2 {

  ArticleService articleService;

  @Autowired
  public ArticleServiceTestWithH2(ArticleService articleService) {
    this.articleService = articleService;
  }

  @Test
  void listArticles_WithNullParam_ReturnsArticleListDtoWithAllArticles() throws Exception {
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

    assertThat(articleService.listArticles(null).getArticles()).usingRecursiveComparison().isEqualTo(articleListDTO.getArticles());
  }

  @Test
  void listArticles_WithStringParam_ReturnsArticleListDtoWithFilteredArticles() throws Exception {
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    article1.setId(1L);

    List<Article> articles = new ArrayList<>();
    articles.add(article1);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    assertThat(articleService.listArticles("News about tickets").getArticles()).usingRecursiveComparison().isEqualTo(articleListDTO.getArticles());
   }
}
















