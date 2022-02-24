package isu.volunteer.api.chat.dto;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Validated
public class AddChatDto {
    @JsonProperty(value = "user id")
    @Positive
    private Long userId;
}
