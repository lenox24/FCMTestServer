package com.example.fcm.repo;

import com.example.fcm.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TokenRepo extends MongoRepository<Token, String> {
    List<Token> findByType(String type);
    List<Token> findByAgree(Boolean agree);
}
