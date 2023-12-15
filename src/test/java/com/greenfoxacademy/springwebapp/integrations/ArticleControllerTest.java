package com.greenfoxacademy.springwebapp.integrations;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ArticleControllerTest {
  @Autowired
  MockMvc mvc;
  @Autowired
  ArticleService articleService;

  @Test
  void listArticles_WithNoParam_ListsAllArticles() throws Exception {
    List<Article> articles = new ArrayList<>();
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    article1.setId(1L);
    Article article2 = new Article("Test Title", "Test Content");
    article2.setPublishDate(LocalDate.of(2023, 12, 11));
    article2.setId(2L);
    articles.add(article1);
    articles.add(article2);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    mvc.perform(get("/api/news"))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.articles").value(hasSize(2)))
        .andExpect(jsonPath("$.articles[0].title").value("News about tickets"));

    assertThat(articleService.listArticles(null)).usingRecursiveComparison().isEqualTo(articleListDTO);
  }

  @Test
  void listArticles_WithSearchParam_ListsSearchedArticlesOnly() throws Exception {
    List<Article> articles = new ArrayList<>();
    Article article1 = new Article("Test Title", "Test Content");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    article1.setId(2L);
    articles.add(article1);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    mvc.perform(get("/api/news").queryParam("search", "Test"))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.articles").value(hasSize(1)))
        .andExpect(jsonPath("$.articles[0].title").value("Test Title"));

    assertThat(articleService.listArticles("Test")).usingRecursiveComparison().isEqualTo(articleListDTO);
  }
}
