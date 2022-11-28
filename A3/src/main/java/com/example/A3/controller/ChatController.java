package com.example.A3.controller;

import com.example.A3.model.ChatMessage;
import com.example.A3.model.Crazy8Game;
import com.example.A3.model.MessageType;
import com.example.A3.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;


@Controller
public class ChatController {
    ArrayList<Player> players = new ArrayList<>();
    Crazy8Game game;

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
        players.add(new Player(headerAccessor.getUser(), chatMessage.getSender()));
        sendingOperations.convertAndSend("/topic/public",chatMessage);
        if(players.size()>=4){
            final ChatMessage newMessage = ChatMessage.builder()
                    .type(MessageType.CHAT)
                    .sender("Dealer")
                    .content("4 Players Have Joined, Game Is Starting")
                    .build();
            sendingOperations.convertAndSend("/topic/public",newMessage);

            ArrayList<String> playerNames = new ArrayList<>();
            for(int i = 0;i< players.size();i++){
                playerNames.add(players.get(i).getName());
            }
            game = new Crazy8Game(playerNames);
        }
    }

}
