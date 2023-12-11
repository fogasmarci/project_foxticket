package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.models.Article;
import com.greenfoxacademy.springwebapp.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicSpringProjectApplication implements CommandLineRunner {
  private ArticleRepository articleRepository;

  @Autowired
  public BasicSpringProjectApplication(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(BasicSpringProjectApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Article article1 = new Article("News about tickets", "Ipsum Lorum");
    Article article2 = new Article("Road block", "akarmi, anything, fdgjkfdkjfdkjfdkjfdkjfdkjfdkj");

    articleRepository.save(article1);
    articleRepository.save(article2);
  }
}

