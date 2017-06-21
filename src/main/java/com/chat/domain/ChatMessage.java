package com.chat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Chatmessage.
 */
@Entity
@Table(name = "chat_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chatmessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "story")
    private String story;

    @Column(name = "senttime")
    private ZonedDateTime senttime;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStory() {
        return story;
    }

    public Chatmessage story(String story) {
        this.story = story;
        return this;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public ZonedDateTime getSenttime() {
        return senttime;
    }

    public Chatmessage senttime(ZonedDateTime senttime) {
        this.senttime = senttime;
        return this;
    }

    public void setSenttime(ZonedDateTime senttime) {
        this.senttime = senttime;
    }

    public Chat getChat() {
        return chat;
    }

    public Chatmessage chat(Chat chat) {
        this.chat = chat;
        return this;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public Chatmessage user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chatmessage chatmessage = (Chatmessage) o;
        if (chatmessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatmessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chatmessage{" +
            "id=" + getId() +
            ", story='" + getStory() + "'" +
            ", senttime='" + getSenttime() + "'" +
            "}";
    }
}
