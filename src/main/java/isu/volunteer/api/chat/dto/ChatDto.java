package isu.volunteer.api.chat.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import isu.volunteer.api.chat.Chat;
import isu.volunteer.api.user.User;
import isu.volunteer.api.user.dto.UserDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatDto {
    private Long id;
    private UserDto user;

    public ChatDto(Chat chat, User issuer) {
        this.id = chat.getId();
        if(chat.getInitiator().equals(issuer)) {
            this.user = new UserDto(chat.getMember());
        } else {
            this.user = new UserDto(chat.getInitiator());
        }
    }

    public static List<ChatDto> convertToChatDto(List<Chat> chats, User issuer) {
        return chats.stream().map(chat -> new ChatDto(chat, issuer)).collect(Collectors.toList());
    }
}
