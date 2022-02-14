package isu.volunteer.api.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import isu.volunteer.api.role.Role;

@Component
public class TokenUtil {
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Value("${jwt.salt}")
    private String salt;

    @Value("${jwt.expired}")
    private long validThru;

    public String createToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        List<String> roleNames = roles.stream()
                                    .map(role -> role.getName())
                                    .collect(Collectors.toList());
        claims.put("roles", roleNames);

        Date now = new Date();
        Date validThru = new Date(now.getTime() + this.validThru);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validThru)
                .signWith(
                    SignatureAlgorithm.HS256,
                    getEncodedSalt(this.salt)
                ).compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7, token.length());
        }
        // throw exception
        return null;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(getEncodedSalt(this.salt)).parseClaimsJws(token);
        if(claims.getBody().getExpiration().before(new Date())) {
            return false;
        }
        return true;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(
            userDetails,
            "",
            userDetails.getAuthorities()
        );
    }

    protected String getUsername(String token) {
        return Jwts.parser().setSigningKey(getEncodedSalt(this.salt)).parseClaimsJws(token).getBody().getSubject();
    }

    protected String getEncodedSalt(String salt) {
        return Base64.getEncoder().encodeToString(salt.getBytes());
    }
}
