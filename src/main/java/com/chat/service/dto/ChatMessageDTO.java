package com.chat.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Chatmessage entity.
 */
public class ChatmessageDTO implements Serializable {

    private Long id;

    private String story;

    private ZonedDateTime senttime;

    private Long chatId;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public ZonedDateTime getSenttime() {
        return senttime;
    }

    public void setSenttime(ZonedDateTime senttime) {
        this.senttime = senttime;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatmessageDTO chatmessageDTO = (ChatmessageDTO) o;
        if(chatmessageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatmessageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatmessageDTO{" +
            "id=" + getId() +
            ", story='" + getStory() + "'" +
            ", senttime='" + getSenttime() + "'" +
            "}";
    }
}
