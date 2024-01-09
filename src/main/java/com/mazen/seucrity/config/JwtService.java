package com.mazen.seucrity.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Claims Mean the date on Payload
//and Wee Will Extract the Username from the Payload
//We have Multiple data on Payload


//JWT SERVICES
@Service
public class JwtService {
    private static final String SECRET_KYE = "";

    public String extractUsername(String token) {

        //Wee, Will Take Username From the token and From token wee Will Take Subject and Subject Contain Username
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);                                                                  //Extract All Claims
        return claimsResolver.apply(claims);                                                                            //Extract singe claim
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    //Generate Token

    public String generateToken(
            Map<String, Object> extraClaims                                                                              //if You want to past any think to token
            ,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)                                                                                 //Add The Claims we want
                .setSubject(userDetails.getUsername())                                                                  //add The Username to Subject on Token
                .setIssuedAt(new Date(System.currentTimeMillis()))                                                      //Date For Generate The Token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))                                   //Date For End Token 24 hours
                .signWith(getSignInKye(), SignatureAlgorithm.HS256)                                                    //Set a SingInKey and The Algorithm
                .compact();

    }

    //Check If The Token Valid Or no
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);                                                                 //Extract username from the token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));                                   //compare if The username from token Belong username on userDetails and token not Expired
    }


    private boolean isTokenExpired(String token) {
        //Check IF the date of Token Before the  Current date
        return extractExpiration(token).before(new Date());
    }

    //Extract The date Expiration From the token
    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }


    //We will Extract all Claims From The Payload On token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKye())                                                                          //Decode The token From The SignKye
                .build()
                .parseClaimsJws(token)                                                                                  // send a token here
                .getBody();                                                                                             // here well Take all Claims From The Body
    }


    //Decode The SingInKey
    private Key getSignInKye() {

        // return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KYE);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}

