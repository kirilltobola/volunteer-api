package isu.volunteer.api.auth;

import lombok.Data;

@Data
public class AuthUserDto {
    private String username;
    private String password;
}
