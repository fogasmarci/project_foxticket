package com.greenfoxacademy.springwebapp.integrations;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

  @Autowired
  MockMvc mvc;
  @MockBean
  ArticleService articleService;

  @Test
  void listArticles_WithNoParam_ListsAllArticles() throws Exception {
    List<Article> articles = new ArrayList<>();
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    article1.setPublishDate(LocalDate.of(2023, 12, 11));
    Article article2 = new Article("Test Title", "Test Content");
    article2.setPublishDate(LocalDate.of(2023, 12, 11));
    articles.add(article1);
    articles.add(article2);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    Mockito.when(articleService.listArticles(null)).thenReturn(articleListDTO);

    mvc.perform(get("/api/news"))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.articles", isA(ArrayList.class)))
        .andExpect(jsonPath("$.articles").value(hasSize(2)))
        .andExpect(jsonPath("$.articles[0].title").value("News about tickets"));
  }

  @Test
  void listArticles_WithParam_ListsSearchedArticles() throws Exception {
    List<Article> articles = new ArrayList<>();

    Article article = new Article("test title", "Test Content");
    article.setPublishDate(LocalDate.of(2023, 12, 11));
    articles.add(article);

    ArticleListDTO articleListDTO = new ArticleListDTO();
    articleListDTO.setArticles(articles);

    Mockito.when(articleService.listArticles("test title")).thenReturn(articleListDTO);

    mvc.perform(get("/api/news").param("search", "test title"))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.articles", isA(ArrayList.class)))
        .andExpect(jsonPath("$.articles").value(hasSize(1)))
        .andExpect(jsonPath("$.articles[0].title").value("test title"));
  }
}












