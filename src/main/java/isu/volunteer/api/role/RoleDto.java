package isu.volunteer.api.role;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto {
    String name;

    public RoleDto(Role role) {
        this.name = role.getName();
    }

    public static List<RoleDto> convertToRoleDto(List<Role> roles) {
        return roles.stream().map(role -> new RoleDto(role)).collect(Collectors.toList());
    }
}
