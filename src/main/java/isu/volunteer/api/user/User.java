package isu.volunteer.api.user;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import isu.volunteer.api.chat.Chat;
import isu.volunteer.api.message.Message;
import isu.volunteer.api.order.Order;
import isu.volunteer.api.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private Date modifiedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @ManyToMany
    private Collection<Chat> chats;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Order> orders;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Collection<Message> messages;

    public void addMessage(Message message) {
        this.messages.add(message);
        message.setUser(this);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
        message.setUser(null);
    }
}
