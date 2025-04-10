package tomaat.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {
    private final String secret;

    public JWTUtil(@Value("${jwt_secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(UUID id) throws IllegalArgumentException, JWTCreationException {
        int JWTExpirationInMinutes = 60;
        return JWT.create()
                .withSubject("User details")
                .withIssuer("Tomaat")
                .withClaim("id", id.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(JWTExpirationInMinutes * 60L)))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        System.out.println(secret);
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Tomaat")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("id").asString();
    }
}
