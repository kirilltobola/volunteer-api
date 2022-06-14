package isu.volunteer.api.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
@AllArgsConstructor
public class RegisterUserDto {
    @NotBlank
    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "medic")
    private Boolean isMedic;

    @NotBlank
    @JsonProperty(value = "firstName")
    private String firstName;

    @NotBlank
    @JsonProperty(value = "lastName")
    private String lastName;

    @NotBlank
    @JsonProperty(value = "phone")
    private String phone;

    @NotBlank
    @Size(min = 8)
    @JsonProperty(value = "password")
    private String password;
}
