package isu.volunteer.api.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AuthUserDto {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;
}
