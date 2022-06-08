package isu.volunteer.api.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isu.volunteer.api.user.User;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatFactory chatFactory;

    public Chat createChat(User issuer, User member) {
        Chat chat = this.chatRepository.findChatByUsers(issuer, member);
        if(chat != null) {
            chat.setInitiator(issuer);
            chat.setVisibleForInitiator(true);
            return chat;
        }

        chat = this.chatFactory.create();
        chat.setInitiator(issuer);
        chat.setVisibleForInitiator(true);
        chat.setMember(member);
        chat.setVisibleForMember(true);

        LocalDateTime now = LocalDateTime.now();
        chat.setCreatedAt(now);
        chat.setModifiedAt(now);
        return this.chatRepository.save(chat);
    }

    public Chat getChat(Chat chat) {
        return this.chatRepository.getById(chat.getId());
    }

    public Chat save(Chat chat) {
        chat.setModifiedAt(LocalDateTime.now());
        return this.chatRepository.save(chat);
    }

    public void delete(Chat chat) {
        this.chatRepository.delete(chat);
    }

    public List<Chat> findVisibleChatsByUser(User user) {
        return this.chatRepository.findVisibleChatsByUser(user);
    }

    public Long countMessages(Chat chat, User issuer) {
        return this.chatRepository.countMessages(chat, issuer);
    }

    public Chat findChatByUsers(User user1, User user2) {
        return this.chatRepository.findChatByUsers(user1, user2);
    }

    public Chat deleteForUser(User user, Chat chat) {
        if(chat.getInitiator().equals(user)) {
            chat.setVisibleForInitiator(false);
        } else {
            chat.setVisibleForMember(false);
        }
        return chat;
    }
}
