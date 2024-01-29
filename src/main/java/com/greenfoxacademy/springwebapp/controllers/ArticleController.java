/*
package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
  private final ArticleService articleService;

  @Autowired
  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping(path = "/api/news")
  public String displayArticlesPage(Model model, @RequestParam String searchKeyword) {
    model.addAttribute("articles", articleService.listArticles(searchKeyword));
    return "article-page";
  }

  @GetMapping(path = "/")
  public String displayMainPage() {
    return "main-page";
  }


}
*/
