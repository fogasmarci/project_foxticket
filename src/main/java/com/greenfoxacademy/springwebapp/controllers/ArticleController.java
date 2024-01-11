package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.AddArticleDTO;
import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.article.ArticleException;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {
  private final ArticleService articleService;

  @Autowired
  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping(path = "/api/news")
  public ResponseEntity<ArticleListDTO> listArticles(@RequestParam(required = false) String search) {
    return ResponseEntity.status(200).body(articleService.listArticles(search));
  }

  @PostMapping(path = "/api/news")
  public ResponseEntity<?> addArticles(@RequestBody AddArticleDTO addArticleDTO) {
    try {
      return ResponseEntity.status(200).body(articleService.addArticle(addArticleDTO));
    } catch (ArticleException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
