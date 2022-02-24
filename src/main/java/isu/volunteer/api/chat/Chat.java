package isu.volunteer.api.chat;


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

import isu.volunteer.api.user.User;
import lombok.Data;


@Data
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(
        name = "initiator_id",
        foreignKey = @ForeignKey(name = "INITIATOR_ID_FK")
    )
    private User initiator;
    
    @Column(
        name = "visible_for_initiator",
        nullable = false,
        columnDefinition = "boolean default true"
    )
    private Boolean visibleForInitiator;

    @ManyToOne
    @JoinColumn(
        name = "member_id",
        foreignKey = @ForeignKey(name = "MEMBER_ID_FK")
    )
    private User member;

    @Column(
        name = "visible_for_member",
        nullable = false,
        columnDefinition = "boolean default true"
    )
    private Boolean visibleForMember;
}
