package isu.volunteer.api.message;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import isu.volunteer.api.chat.Chat;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByChat(Chat chat);
}
