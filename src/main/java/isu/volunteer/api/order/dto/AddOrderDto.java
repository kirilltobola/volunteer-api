package isu.volunteer.api.order.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.validation.annotation.Validated;

import isu.volunteer.api.order.Address;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
public class AddOrderDto {
    @NotBlank
    private String headline;

    private Address address;
    
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime date;
    
    private String comment;
    
    private String status;
}
