package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.AddArticleDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.article.ArticleException;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {
  private final ArticleService articleService;

  @Autowired
  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping("/api/news")
  public ResponseEntity<ArticleListDTO> listArticles(@RequestParam(required = false) String search) {
    return ResponseEntity.status(200).body(articleService.listArticles(search));
  }

  @PostMapping("/api/news")
  public ResponseEntity<?> addArticle(@RequestBody AddArticleDTO addArticleDTO) {
    try {
      return ResponseEntity.status(200).body(articleService.addArticle(addArticleDTO));
    } catch (ArticleException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @PutMapping("/api/news/{articleId}")
  public ResponseEntity<?> editArticle(@RequestBody AddArticleDTO addArticleDTO, @PathVariable Long articleId) {
    try {
      return ResponseEntity.status(200).body(articleService.editArticle(addArticleDTO, articleId));
    } catch (ArticleException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @DeleteMapping("/api/news/{articleId}")
  public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
    try {
      return ResponseEntity.status(200).body(articleService.deleteArticle(articleId));
    } catch (ArticleException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
