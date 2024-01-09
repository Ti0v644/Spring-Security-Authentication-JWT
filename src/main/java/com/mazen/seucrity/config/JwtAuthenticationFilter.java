package com.mazen.seucrity.config;


import com.mazen.seucrity.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
//The Request  Will come here and check for ever request
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            //Shod not to bee null
            @NonNull HttpServletRequest request,                                                     //We can edit Current Request or Response from here
            @NonNull HttpServletResponse response,                                                   //The filters
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");                        //We will Extract the Token From a Header
        final String jwt;
        final String userEmail;


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {                                 //First wee need to Check if Token not exist
            filterChain.doFilter(request, response);                                              //if token not exist go to another Filter
            return;
        }


        // Mean we have a token and extract the token from Header
        jwt = authHeader.substring(7);


        userEmail = jwtService.extractUsername(jwt);                                           //Extract UserName valid


        // the user not Authenticated SecurityContextHolder.getContext().getAuthentication()==null
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //user not connected
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);


            //check If Token is Not Expired and not Revoked
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(token -> !token.isExpired() && !token.isRevoked()).orElse(false);


            //if the token is Valid
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {

                //Create send it to spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //Update the SecurityContextHolder To bee Authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //send it to another filter
        filterChain.doFilter(request, response);

    }
}
