package com.finalAPI.farmerLoginSignupApi.repository;


import com.finalAPI.farmerLoginSignupApi.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token,String> {
    @Query("{'user.id': ?0, 'loggedOut' : false}")
    List<Token> findAllTokenByUser(String userId);

    Optional<Token> findByToken(String token);
}
