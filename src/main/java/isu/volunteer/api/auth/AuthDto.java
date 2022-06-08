package isu.volunteer.api.auth;

import java.util.List;

import isu.volunteer.api.role.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthDto {
    private Long id;
    private String username;
    private List<RoleDto> roles;
    private TokenDto token;
}
