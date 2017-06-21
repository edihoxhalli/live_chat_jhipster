package com.chat.service.mapper;

import com.chat.domain.*;
import com.chat.service.dto.ChatmessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Chatmessage and its DTO ChatmessageDTO.
 */
@Mapper(componentModel = "spring", uses = {ChatMapper.class, UserMapper.class, })
public interface ChatmessageMapper extends EntityMapper <ChatmessageDTO, Chatmessage> {

    @Mapping(source = "chat.id", target = "chatId")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ChatmessageDTO toDto(Chatmessage chatmessage); 

    @Mapping(source = "chatId", target = "chat")

    @Mapping(source = "userId", target = "user")
    Chatmessage toEntity(ChatmessageDTO chatmessageDTO); 
    default Chatmessage fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chatmessage chatmessage = new Chatmessage();
        chatmessage.setId(id);
        return chatmessage;
    }
}
