package mx.com.ml.rebell.alliance.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtToken {

  private static final long JWT_TOKEN_VALIDITY = (10 * 60 * 1000);

  private static final long REFRESH_TOKEN_VALIDITY = (60 * 60 * 1000);

  private static final String PREFIX_TOKEN = "Bearer ";

  private static final String SECRET_KEY = "ml_alliance_authorization_server";

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return PREFIX_TOKEN.concat(doGenerateToken(claims, userDetails.getUsername(), JWT_TOKEN_VALIDITY));
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("token_type", "refresh");
    return doGenerateToken(claims, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY);
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  private Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  private String doGenerateToken(Map<String, Object> claims, String subject, Long tokenValidity) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }
}
