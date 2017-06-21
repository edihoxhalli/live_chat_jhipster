package com.chat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Chat.
 */
@Entity
@Table(name = "chat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creationtime")
    private ZonedDateTime creationtime;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationtime() {
        return creationtime;
    }

    public Chat creationtime(ZonedDateTime creationtime) {
        this.creationtime = creationtime;
        return this;
    }

    public void setCreationtime(ZonedDateTime creationtime) {
        this.creationtime = creationtime;
    }

    public User getUser1() {
        return user1;
    }

    public Chat user1(User user) {
        this.user1 = user;
        return this;
    }

    public void setUser1(User user) {
        this.user1 = user;
    }

    public User getUser2() {
        return user2;
    }

    public Chat user2(User user) {
        this.user2 = user;
        return this;
    }

    public void setUser2(User user) {
        this.user2 = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chat chat = (Chat) o;
        if (chat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chat{" +
            "id=" + getId() +
            ", creationtime='" + getCreationtime() + "'" +
            "}";
    }
}
