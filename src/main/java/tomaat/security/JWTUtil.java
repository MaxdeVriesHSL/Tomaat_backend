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
import tomaat.model.User;

@Component
public class JWTUtil {
    private static final int JWT_EXPIRATION_MINUTES = 60;
    private static final String DEFAULT_ROLE = "USER";

    private final String secret;
    private final Algorithm algorithm;

    public JWTUtil(@Value("${jwt_secret}") String secret) {
        this.secret = secret;
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(UUID id) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User details")
                .withIssuer("Tomaat")
                .withClaim("id", id.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    public String generateToken(User user) throws IllegalArgumentException, JWTCreationException {
        String roleName = user.getRole() != null ? user.getRole().getName() : DEFAULT_ROLE;

        return JWT.create()
                .withSubject("User details")
                .withIssuer("Tomaat")
                .withClaim("id", user.getUUID().toString())
                .withClaim("email", user.getEmail())
                .withClaim("role", roleName)
                .withIssuedAt(new Date())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm)
                .withSubject("User details")
                .withIssuer("Tomaat")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("id").asString();
    }

    private Date getExpirationDate() {
        return Date.from(Instant.now().plusSeconds(JWT_EXPIRATION_MINUTES * 60L));
    }
}