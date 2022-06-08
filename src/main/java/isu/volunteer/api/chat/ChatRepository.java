package isu.volunteer.api.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isu.volunteer.api.message.Message;
import isu.volunteer.api.user.User;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "SELECT m FROM Message m WHERE m.chat = :chat")
    List<Message> getMessages(@Param("chat") Chat chat);

    @Query(value = "SELECT COUNT(m) FROM Message m WHERE m.chat = :chat AND m.sender != :issuer")
    Long countMessages(@Param("chat") Chat chat, @Param("issuer") User issuer);

    @Query(value = "SELECT c FROM Chat c WHERE :user = c.initiator AND c.visibleForInitiator = 1 OR :user = c.member AND c.visibleForMember = 1")
    List<Chat> findVisibleChatsByUser(@Param("user") User user);

    @Query(value = "SELECT c FROM Chat c WHERE :issuer in (c.initiator, c.member) AND :member in (c.initiator, c.member)")
    Chat findChatByUsers(@Param("issuer") User issuer, @Param("member") User member);
}
