package com.lucky.VehicleManagementSystem.security.jwt;

import com.lucky.VehicleManagementSystem.exceptions.JWTVerificationException;
import com.lucky.VehicleManagementSystem.security.user.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTUtils {
    @Value("${application.security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${application.security.jwt.expiration}")
    private String jwtAccessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private String jwtRefreshTokenExpiration;

    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String CLAIM_KEY_EMAIL = "email";
    private static final String CLAIM_KEY_ROLE = "role";

//    generating secret key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    function to help us get claims from token (userId,email,role)
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
    }

//    another utility function to help us extract claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

//    extracting the username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

//    extracting the expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

//    getting boolean for token expiration
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

//    checking validity of token
    public boolean isTokenValid(String token, UserPrincipal userPrincipal) {
        String email = (String) extractAllClaims(token).get(CLAIM_KEY_EMAIL);
        return email.equals(userPrincipal.getUsername()) && !isTokenExpired(token);
    }

//    generating accessToken
    public String generateToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime()+Long.parseLong(jwtAccessTokenExpiration));

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setId(userPrincipal.getId().toString())
                .setSubject(userPrincipal.getUsername())
                .claim(CLAIM_KEY_USER_ID,userPrincipal.getId().toString())
                .claim(CLAIM_KEY_EMAIL,userPrincipal.getEmail())
                .claim(CLAIM_KEY_ROLE,roles)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    generating refreshToken
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshTokenExpiration);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim(CLAIM_KEY_USER_ID, userPrincipal.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

//    decoding the token
    public JwtExtractedUser decodeToken(String token) throws JWTVerificationException {
        Claims claims = extractAllClaims(token);
        UUID userId = UUID.fromString((String) claims.get(CLAIM_KEY_USER_ID));
        String email = (String) claims.get(CLAIM_KEY_EMAIL);
        List<String> role = (List<String>) claims.get(CLAIM_KEY_ROLE);

        return new JwtExtractedUser()
                .setUserId(userId)
                .setEmail(email)
                .setRole(role);
    }
}
