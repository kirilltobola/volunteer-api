package isu.volunteer.api.chat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isu.volunteer.api.PropertiesCopier;
import isu.volunteer.api.chat.dto.AddChatDto;
import isu.volunteer.api.chat.dto.ChatDto;
import isu.volunteer.api.jwt.TokenUtil;
import isu.volunteer.api.message.Message;
import isu.volunteer.api.message.MessageFactory;
import isu.volunteer.api.message.MessageService;
import isu.volunteer.api.message.dto.AddMessageDto;
import isu.volunteer.api.message.dto.MessageDto;
import isu.volunteer.api.user.User;
import isu.volunteer.api.user.UserService;

@RestController
@RequestMapping(value = "/api/v1/chats")
@Validated
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private PropertiesCopier<Message, AddMessageDto> propertiesCopier;

    @PostMapping()
    public ResponseEntity<Object> startChat(
        @RequestBody @Valid AddChatDto chatDto,
        @RequestHeader("Authorization") String token
    ) {
        Map<Object, Object> response = new HashMap<>();
        
        User issuer = this.tokenUtil.getUser(token);
        User user = this.userService.findById(chatDto.getUserId());
        Chat foundedChat = this.chatService.findChatByUsers(issuer, user);

        if (issuer.getId() != chatDto.getUserId() && foundedChat == null) {
            Chat chat = this.chatService.createChat(issuer, user);
            chat.setCreatedAt(LocalDateTime.now());
            chat.setModifiedAt(LocalDateTime.now());
            return ResponseEntity.ok(new ChatDto(this.chatService.save(chat), issuer));
        } else {
            foundedChat.setVisibleForInitiator(true);
            foundedChat.setVisibleForMember(true);
            this.chatService.save(foundedChat);
            return ResponseEntity.ok(new ChatDto(foundedChat, issuer));
        }
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<Object> sendMessage(
        @RequestHeader("Authorization") String token,
        @RequestBody @Valid AddMessageDto messageDto,
        @PathVariable("id") Chat chat
    ) {
        User issuer = this.tokenUtil.getUser(token);
        if(chat.getInitiator().equals(issuer) || chat.getMember().equals(issuer)) {
            Map<Object, Object> response = new HashMap<>();

            Message message = this.messageFactory.create();
            this.propertiesCopier.copyProperties(message, messageDto);
            
            message.setSender(issuer);
            message = this.messageService.sendMessage(chat, message);
            message.setCreatedAt(LocalDateTime.now());
            message.setModifiedAt(LocalDateTime.now());
            this.messageService.save(message);

            response.put("status", "message has been succesfully sent");
            return ResponseEntity.ok(response);
        }
        throw new AccessDeniedException("You are not in the chat.");
        
    }

    @GetMapping(path = "/{id}/messages")
    public ResponseEntity<Object> getChatMessages(
        @RequestHeader("Authorization") String token,
        @PathVariable("id") Chat chat
    ) {
        User issuer = this.tokenUtil.getUser(token);
        if(chat.getInitiator().equals(issuer) || chat.getMember().equals(issuer)) {
            Map<Object, Object> response = new HashMap<>();

            List<Message> messages = this.messageService.findByChat(chat);
            List<MessageDto> messageDtos = MessageDto.convertToDto(messages);

            response.put("messages", messageDtos);
            return ResponseEntity.ok(messageDtos); // TODO: delete response
        }
        throw new AccessDeniedException("You are not in the chat.");
        
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getChat(
        @RequestHeader("Authorization") String token,
        @PathVariable("id") Chat chat
    ) { 
        User issuer = this.tokenUtil.getUser(token);
        if(chat.getInitiator().equals(issuer) || chat.getMember().equals(issuer)) {
            return ResponseEntity.ok(new ChatDto(chat, issuer));
        }
        throw new AccessDeniedException("You are not in the chat.");
    }

    @GetMapping(path = "/{id}/count-messages")
    public ResponseEntity<Object> countMessages(
        @RequestHeader("Authorization") String token,
        @PathVariable("id") Chat chat
    ) {
        Map<Object, Object> response = new HashMap<>();
        User issuer = this.tokenUtil.getUser(token);

        Long messagesCount = this.chatService.countMessages(chat, issuer);
        response.put("messages", messagesCount); // TODO make dto or smthg
        return ResponseEntity.ok(messagesCount);
    }

    @GetMapping()
    public ResponseEntity<Object> getChats(@RequestHeader("Authorization") String token) {
        Map<Object, Object> response = new HashMap<>();

        User issuer = this.tokenUtil.getUser(token);
        List<Chat> chats = this.chatService.findVisibleChatsByUser(issuer);
        List<ChatDto> chatDtos = ChatDto.convertToChatDto(chats, issuer);

        response.put("chats", chatDtos);
        return ResponseEntity.ok(chatDtos);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteChat(
        @RequestHeader("Authorization") String token, 
        @PathVariable("id") Chat chat
        ) {
        Map<Object, Object> response = new HashMap<>();

        User issuer = this.tokenUtil.getUser(token);
        chat = this.chatService.deleteForUser(issuer, chat);
        this.chatService.save(chat);

        response.put("status", "chat has been deleted.");
        return ResponseEntity.ok(response);
    }
}
