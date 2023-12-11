package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Override
    List<Article> findAll();

    @Query(nativeQuery = true, value = "SELECT * FROM news WHERE title LIKE CONCAT('%',:keyword,'%') OR content LIKE CONCAT('%',:keyword,'%')")
    List<Article> findArticlesByKeyword(String keyword);
}
