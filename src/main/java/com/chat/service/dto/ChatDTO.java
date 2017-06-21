package com.chat.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Chat entity.
 */
public class ChatDTO implements Serializable {

    private Long id;

    private ZonedDateTime creationtime;

    private Long user1Id;

    private String user1Login;

    private Long user2Id;

    private String user2Login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(ZonedDateTime creationtime) {
        this.creationtime = creationtime;
    }

    public Long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Long userId) {
        this.user1Id = userId;
    }

    public String getUser1Login() {
        return user1Login;
    }

    public void setUser1Login(String userLogin) {
        this.user1Login = userLogin;
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Long userId) {
        this.user2Id = userId;
    }

    public String getUser2Login() {
        return user2Login;
    }

    public void setUser2Login(String userLogin) {
        this.user2Login = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatDTO chatDTO = (ChatDTO) o;
        if(chatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatDTO{" +
            "id=" + getId() +
            ", creationtime='" + getCreationtime() + "'" +
            "}";
    }
}
