package com.mazen.seucrity.token;

import com.mazen.seucrity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository  extends JpaRepository<Token,Integer> {


    Optional<Token> findByToken(String token);

    List<Token> findByUserIdAndExpiredFalseAndRevokedFalse(Integer userId);

}
