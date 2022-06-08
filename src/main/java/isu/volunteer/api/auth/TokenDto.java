package isu.volunteer.api.auth;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TokenDto {
    private String token;
    private Date expiresIn;
    
    public TokenDto(String token, Date expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
