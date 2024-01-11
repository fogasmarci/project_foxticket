package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.AddArticleDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.security.JwtValidatorService;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ArticleControllerTest {
  @Autowired
  MockMvc mvc;
  @Autowired
  ArticleService articleService;
  @Autowired
  JwtValidatorService jwtValidatorService;
  ObjectMapper objectMapper = new ObjectMapper();

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

  @Test
  void addArticles_NotLoggedIn_ReturnsUnauthorized() throws Exception {
    AddArticleDTO addArticleDTO = new AddArticleDTO("What is the future of travelling", "Something something about future of travel");

    mvc.perform(post("/api/news")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addArticleDTO)))
        .andExpect(status().is(401));
  }

  @Test
  void addArticles_WithLoggedUser_ReturnsForbidden() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    AddArticleDTO addArticleDTO = new AddArticleDTO("What is the future of travelling", "Something something about future of travel");

    mvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addArticleDTO)))
        .andExpect(status().is(403));
  }

  @Test
  void addArticles_WithLoggedAdminAndNullTitle_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    AddArticleDTO addArticleDTO = new AddArticleDTO(null, "content");

    mvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addArticleDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Title is required"));
  }

  @Test
  void addArticles_WithLoggedAdminAndNullContent_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    AddArticleDTO addArticleDTO = new AddArticleDTO("title", null);

    mvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addArticleDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Content is required"));
  }

  @Test
  void addArticles_WithLoggedAdminAndAlreadyTakenTitle_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    AddArticleDTO addArticleDTO = new AddArticleDTO("Test Title", "Test Content");

    mvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addArticleDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("News title already exists"));
  }

  @Test
  void addArticles_WithLoggedAdminAndCorrectRequest_ReturnsAddedArticle() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    AddArticleDTO addArticleDTO = new AddArticleDTO("newtitle", "newcontent");

    mvc.perform(post("/api/news").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addArticleDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.title").value(addArticleDTO.getTitle()))
        .andExpect(jsonPath("$.content").value(addArticleDTO.getContent()));

  }

  private String login(LoginUserDTO loginUserDTO) throws Exception {
    String responseContent = mvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.token").exists())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Map<String, String> map = objectMapper.readValue(responseContent, Map.class);
    return map.get("token");
  }
}
