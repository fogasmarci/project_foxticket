package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import com.greenfoxacademy.springwebapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(path = "/api/news", method = RequestMethod.GET)
    public ResponseEntity<ArticleListDTO> listArticles(@RequestParam(required = false) String search) {
        return ResponseEntity.status(200).body(articleService.listArticles(search));
    }
}















