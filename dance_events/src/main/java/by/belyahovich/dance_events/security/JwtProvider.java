package by.belyahovich.dance_events.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${tokenSecret}")
    private String jwtSecret;

    @Value("${tokenLifetime}")
    private int tokenLifetime;


    public String generateToken(String login) {
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now()
                .plusMinutes(tokenLifetime)
                .atZone(ZoneId.systemDefault()).toInstant());
        String token = "";
        try {
            token = Jwts.builder()
                    .setSubject(login)
                    .setIssuedAt(now)
                    .setNotBefore(now)
                    .setExpiration(exp)
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


}
