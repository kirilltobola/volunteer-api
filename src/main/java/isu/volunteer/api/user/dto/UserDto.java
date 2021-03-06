package isu.volunteer.api.user.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import isu.volunteer.api.role.Role;
import isu.volunteer.api.role.RoleDto;
import isu.volunteer.api.user.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;

    private String username;

    @JsonProperty("first name")
    private String firstName;

    @JsonProperty("last name")
    private String lastName;

    private String email;

    private String phone;

    private LocalDateTime createdAt;

    private List<RoleDto> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
        this.roles = convertRolesToRoleDtos(user.getRoles());
    }

    protected List<RoleDto> convertRolesToRoleDtos(List<Role> roles) {
        return roles.stream().map(role -> new RoleDto(role)).collect(Collectors.toList());
    }
}
