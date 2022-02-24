package isu.volunteer.api.order.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
public class EditStatusDto {
    @NotBlank
    private String status;
}
