package com.example.A3.controller;

import com.example.A3.model.ChatMessage;
import com.example.A3.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ChatController {
    ArrayList<Principal> players = new ArrayList<>();
    int direction = 1;

    @Autowired
    private SimpMessageSendingOperations sendingOperations;


    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public void sendMessage(@Payload final ChatMessage chatMessage){
        sendingOperations.convertAndSend("/topic/public",chatMessage);
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public void newUser(@Payload final ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){

        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        players.add(headerAccessor.getUser());
        sendingOperations.convertAndSend("/topic/public",chatMessage);
        if(players.size()>=4){
            final ChatMessage newMessage = ChatMessage.builder()
                    .type(MessageType.CHAT)
                    .sender("Dealer")
                    .content("4 Players Have Joined, Game Is Starting")
                    .build();
            sendingOperations.convertAndSend("/topic/public",newMessage);
        }
    }

}
