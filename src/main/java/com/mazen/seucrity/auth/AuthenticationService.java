package com.mazen.seucrity.auth;

import com.mazen.seucrity.config.JwtService;
import com.mazen.seucrity.token.Token;
import com.mazen.seucrity.token.TokenRepository;
import com.mazen.seucrity.token.TokenType;
import com.mazen.seucrity.user.Role;
import com.mazen.seucrity.user.User;
import com.mazen.seucrity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


    public AuthenticationResponse register(DtoReg request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var SavedUser = repository.save(user);

        var jwtToken = jwtService.generateToken(user);


        revokeAllTokenForUser(SavedUser.getId());
        savedTokenAndUser(SavedUser, jwtToken);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }


    public AuthenticationResponse authenticate(DtoLogin request) {
   // ---------------------------------------------------------------------------//
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        //Here Mean the User are Authenticate
   // ---------------------------------------------------------------------------//

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        revokeAllTokenForUser(user.getId());
        savedTokenAndUser(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    private void revokeAllTokenForUser(Integer id) {
        var allValidToken = tokenRepository.findByUserIdAndExpiredFalseAndRevokedFalse(id);
        if (allValidToken.isEmpty())
            return;
        allValidToken.forEach(v -> {
            v.setRevoked(true);
            v.setExpired(true);
        });
        tokenRepository.saveAll(allValidToken);
    }

    private void savedTokenAndUser(User user, String jwtToken) {
        var saveToken = Token.builder()
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .token(jwtToken)
                .build();
        tokenRepository.save(saveToken);
    }
}
