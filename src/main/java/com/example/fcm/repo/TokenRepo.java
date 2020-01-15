package com.example.fcm.repo;

import com.example.fcm.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TokenRepo extends MongoRepository<Token, String> {
    List<Token> findByType(String type);

    List<Token> findByAgree(String agree);

    String findByToken(String token);
}
