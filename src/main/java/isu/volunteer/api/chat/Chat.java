package isu.volunteer.api.chat;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import isu.volunteer.api.message.Message;
import isu.volunteer.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private Date modifiedAt;

    @ManyToMany(mappedBy = "chats")
    private Collection<User> users;

    @OneToMany(
        mappedBy = "chat",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Collection<Message> messages;

    public void addMessage(Message message) {
        messages.add(message);
        message.setChat(this);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        message.setChat(null);
    }
}
