package com.chat.service.mapper;

import com.chat.domain.*;
import com.chat.service.dto.ChatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Chat and its DTO ChatDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface ChatMapper extends EntityMapper <ChatDTO, Chat> {

    @Mapping(source = "user1.id", target = "user1Id")
    @Mapping(source = "user1.login", target = "user1Login")

    @Mapping(source = "user2.id", target = "user2Id")
    @Mapping(source = "user2.login", target = "user2Login")
    ChatDTO toDto(Chat chat); 

    @Mapping(source = "user1Id", target = "user1")

    @Mapping(source = "user2Id", target = "user2")
    Chat toEntity(ChatDTO chatDTO); 
    default Chat fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chat chat = new Chat();
        chat.setId(id);
        return chat;
    }
}
