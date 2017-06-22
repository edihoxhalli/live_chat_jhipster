package com.chat.service.mapper;

import com.chat.domain.*;
import com.chat.service.dto.ChatMessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ChatMessage and its DTO ChatMessageDTO.
 */
@Mapper(componentModel = "spring", uses = {ChatMapper.class, UserMapper.class, })
public interface ChatMessageMapper extends EntityMapper <ChatMessageDTO, ChatMessage> {

    @Mapping(source = "chat.id", target = "chatId")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ChatMessageDTO toDto(ChatMessage chatmessage); 

    @Mapping(source = "chatId", target = "chat")

    @Mapping(source = "userId", target = "user")
    ChatMessage toEntity(ChatMessageDTO chatmessageDTO); 
    default ChatMessage fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatMessage chatmessage = new ChatMessage();
        chatmessage.setId(id);
        return chatmessage;
    }
}
