package isu.volunteer.api.message;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isu.volunteer.api.chat.Chat;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Chat chat, Message message) {
        message.setChat(chat);
        return message;
    }

    public Message save(Message message) {
        message.setModifiedAt(LocalDateTime.now());
        return this.messageRepository.save(message);
    }

    public List<Message> findByChat(Chat chat) {
        return this.messageRepository.findByChat(chat);
    }
}
