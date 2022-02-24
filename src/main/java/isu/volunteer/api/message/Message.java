package isu.volunteer.api.message;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import isu.volunteer.api.chat.Chat;
import isu.volunteer.api.user.User;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(
        name = "sender_id",
        foreignKey = @ForeignKey(name = "SENDER_ID_FK")
    )
    private User sender;

    @ManyToOne
    @JoinColumn(
        name = "chat_id",
        foreignKey = @ForeignKey(name = "CHAT_ID_FK")
    )
    private Chat chat;
}
