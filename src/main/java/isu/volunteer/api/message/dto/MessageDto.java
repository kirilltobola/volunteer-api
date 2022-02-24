package isu.volunteer.api.message.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import isu.volunteer.api.message.Message;
import isu.volunteer.api.user.dto.UserDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto {
    private String body;
    private UserDto sender;

    public MessageDto(Message message) {
        this.body = message.getBody();
        this.sender = new UserDto(message.getSender());
    }

    public static List<MessageDto> convertToDto(List<Message> messages) {
        return messages.stream().map(message -> new MessageDto(message)).collect(Collectors.toList());
    }
}
