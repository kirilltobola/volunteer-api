package isu.volunteer.api.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
@AllArgsConstructor
public class RegisterUserDto {
    @NotBlank
    private String username;

    private Boolean isMedic;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    @Size(min = 8)
    private String password;
}
