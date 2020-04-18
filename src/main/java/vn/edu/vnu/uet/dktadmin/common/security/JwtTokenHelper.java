package vn.edu.vnu.uet.dktadmin.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.model.DktStudent;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtTokenHelper {
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static String SECRET_KEY;
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String ROLE = "role";
    static final String ID = "id";
    static final String USERNAME = "username";
    static final String EMAIL = "email";

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${dkt.secret.key}")
    public void setPortalSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public String generateTokenStudent(DktAdmin admin) {
        String token = null;
        admin.setRole("Admin");
        token = Jwts.builder()
                .claim(USERNAME, admin.getUsername())
                .claim(EMAIL, admin.getEmail())
                .claim(ROLE, admin.getRole())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return TOKEN_PREFIX + token;
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        return getAuthentication(token);
    }

    private static Authentication getAuthentication(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) return null;
        Authentication auth = null;
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String role = claims.get(ROLE).toString();
            String username = claims.get(USERNAME).toString();
            String email = claims.get(EMAIL).toString();
            Collection authorities =
                    Arrays.stream(claims.get(ROLE).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            DktAdmin admin = DktAdmin.builder()
                    .username(username)
                    .email(email)
                    .build();
            auth = new UsernamePasswordAuthenticationToken(admin,null,authorities);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return auth;
    }
}
