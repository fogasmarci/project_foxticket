package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.AddArticleDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleRestController {
  private final ArticleService articleService;

  @Autowired
  public ArticleRestController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping("/api/news")
  public ResponseEntity<ArticleListDTO> listArticles(@RequestParam(required = false, name = "searchKeyword") String search) {
    return ResponseEntity.status(200).body(articleService.listArticles(search));
  }

  @PostMapping("/api/news")
  public ResponseEntity<Article> addArticle(@RequestBody AddArticleDTO addArticleDTO) {
    return ResponseEntity.status(200).body(articleService.addArticle(addArticleDTO));
  }

  @PutMapping("/api/news/{articleId}")
  public ResponseEntity<Article> editArticle(@RequestBody AddArticleDTO addArticleDTO, @PathVariable Long articleId) {
    return ResponseEntity.status(200).body(articleService.editArticle(addArticleDTO, articleId));
  }

  @DeleteMapping("/api/news/{articleId}")
  public ResponseEntity<MessageDTO> deleteArticle(@PathVariable Long articleId) {
    return ResponseEntity.status(200).body(articleService.deleteArticle(articleId));
  }
}
