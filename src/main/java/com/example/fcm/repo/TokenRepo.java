package com.example.fcm.repo;

import com.example.fcm.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepo extends MongoRepository<Token, String> {
}
