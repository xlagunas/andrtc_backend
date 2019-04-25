package cat.xlagunas.andrtc.authentication;

import java.io.Serializable;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import cat.xlagunas.andrtc.user.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    private static final long serialVersionUID = -3301605591108950415L;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Instant getCreatedInstantFromToken(String token) {
        Instant created;
        try {
            final Claims claims = getClaimsFromToken(token);
            LinkedHashMap<String, Integer> hashMap = ((LinkedHashMap) getClaimsFromToken(token).get(CLAIM_KEY_CREATED));
            created = Instant.ofEpochSecond(hashMap.get("epochSecond"), hashMap.get("nano"));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Instant getExpirationInstantFromToken(String token) {
        Instant expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration().toInstant();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Instant expiration = getExpirationInstantFromToken(token);
        return expiration.isBefore(Calendar.getInstance().toInstant());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());

        final Instant createdDate = Calendar.getInstance().toInstant();
        claims.put(CLAIM_KEY_CREATED, createdDate);

        return doGenerateToken(claims);
    }

    private String doGenerateToken(Map<String, Object> claims) {
        final Instant createdInstant = (Instant) claims.get(CLAIM_KEY_CREATED);
        final Instant expirationInstant = createdInstant.plusMillis(expiration * 1000);

        System.out.println("doGenerateToken " + createdInstant);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expirationInstant))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, Calendar.getInstance().toInstant());
            refreshedToken = doGenerateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        UserDto user = (UserDto) userDetails;
        final String username = getUsernameFromToken(token);
        final Instant created = getCreatedInstantFromToken(token);

        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.passwordUpdate));
    }

    private Boolean isCreatedBeforeLastPasswordReset(Instant created, Instant lastPasswordReset) {
        return (lastPasswordReset != null && created.isBefore(lastPasswordReset));
    }
}