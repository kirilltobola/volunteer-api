package isu.volunteer.api.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto {
    String name;

    public RoleDto(Role role) {
        this.name = role.getName();
    }
}
