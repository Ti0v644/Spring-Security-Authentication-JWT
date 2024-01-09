package com.mazen.seucrity.config;

import com.mazen.seucrity.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        //  "Authorization"
        //    "Bearer "
        final String Header = request.getHeader("Authorization");
        if (Header == null && !Header.startsWith("Bearer ")) {
            return;
        }
        String token = Header.substring(7);
        var CurrentToken = tokenRepository.findByToken(token).orElse(null);
        if(CurrentToken!=null){
        CurrentToken.setExpired(true);
        CurrentToken.setRevoked(true);
        tokenRepository.save(CurrentToken);
        }


        // Mean we have a token and extract the token from Header to jwt

    }
}
