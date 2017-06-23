package com.chat.web.websocket;

import static com.chat.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chat.service.ChatService;
import com.chat.service.dto.ChatDTO;
import com.chat.service.dto.ChatMessageDTO;
import com.chat.web.websocket.dto.ActivityDTO;

@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    
    public ActivityService(SimpMessageSendingOperations messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @SubscribeMapping("/topic/activity")
    @SendTo("/topic/tracker")
    public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        activityDTO.setUserLogin(principal.getName());
        activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
        activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        activityDTO.setTime(Instant.now());
        log.debug("Sending user tracking data {}", activityDTO);
        return activityDTO;
    }
    
    @SubscribeMapping("/chat/update-window")
    //@SendTo("/chat/new-message/{id}")
    public void updateChat(@Payload ChatMessageDTO chatmessageDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
    	System.out.println("enter enter");
    	//ChatMessageDTO chatmessageDTO = new ChatMessageDTO(); 
    	chatmessageDTO.setStory("default story");
    	System.out.println(chatmessageDTO);
    	this.sendMessage(chatmessageDTO, chatmessageDTO.getChatId());
    }
    
    //@Scheduled(fixedDelay=1000)
    public void sendMessage(ChatMessageDTO chatmessageDTO, Long chatId) {
    	String recepient = "";
    	String outputUrl = "/chat/new-message/"+chatId;
    	ChatDTO currentChat = chatService.findOne(chatId);
    	if(currentChat.getUser1Login().equals(chatmessageDTO.getUserLogin()))
    		recepient = currentChat.getUser2Login();
    	else
    		recepient = currentChat.getUser1Login();
    	System.out.println("Sending Sending "+ recepient +  outputUrl);
    	this.messagingTemplate.convertAndSendToUser(recepient, outputUrl, chatmessageDTO);
    	this.messagingTemplate.convertAndSend(outputUrl, chatmessageDTO);
    }


    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setSessionId(event.getSessionId());
        activityDTO.setPage("logout");
        messagingTemplate.convertAndSend("/topic/tracker", activityDTO);
    }
}
