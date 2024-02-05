package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
  private final ArticleService articleService;

  @Autowired
  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping("/news")
  public String displayAllArticlesPage(Model model, @RequestParam(required = false, name = "searchKeyword") String searchKeyword) {
    model.addAttribute("articles", articleService.listArticles(searchKeyword));
    return "article-page";
  }
}

