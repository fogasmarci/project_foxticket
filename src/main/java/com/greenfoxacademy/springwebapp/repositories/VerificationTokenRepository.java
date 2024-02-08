package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
}
