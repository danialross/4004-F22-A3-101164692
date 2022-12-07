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
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ChatController {
    ArrayList<Player> players = new ArrayList<>();
    Crazy8Game game;
    boolean waitingForResponse = false;

    @Autowired
    private SimpMessageSendingOperations sendingOperations;


    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public void sendMessage(@Payload final ChatMessage chatMessage){
        //test cases for part 1 and part 2
        if(chatMessage.getSender().equals(game.getPlayers().get(0).getName()) && chatMessage.getContent().equals("TEST_ROW_41")){
            game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3C")));
            game.setCurrentTopCard("4C");
            dealerSendMessagetoUser("Initialized Test Row 41",game.getPlayers().get(0).getUser().getName());
            dealerSendMessagetoUser("Your hand : " + game.getPlayers().get(0).getHand().toString(),game.getPlayers().get(0).getUser().getName());
            sendCurrentCardAndTurn();
            return;
        }else if(chatMessage.getSender().equals(game.getPlayers().get(0).getName()) && chatMessage.getContent().equals("TEST_ROW_45")){
            game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3C")));
            game.setCurrentTopCard("4C");
            game.setCurrPlayerIndex(3);
            dealerSendMessagetoUser("Initialized Test Row 45",game.getPlayers().get(0).getUser().getName());
            dealerSendMessagetoUser("Your hand : " + game.getPlayers().get(3).getHand().toString(),game.getPlayers().get(3).getUser().getName());
            sendCurrentCardAndTurn();
            return;
        }




        if(chatMessage.getSender().equals(game.getPlayers().get(game.getCurrPlayerIndex()).getName())){

            //less than 0 DNE
            //more than 5 == invalid
            //accept only 2 or 5
            //reject everything else
            String[] response = new String[2];
            System.out.println(chatMessage.getContent().length());

            if(waitingForResponse && chatMessage.getContent().length()==2){
                waitingForResponse = false;
                game.changeSuit(chatMessage.getContent());
            }

            if(chatMessage.getContent().length()!=5 && chatMessage.getContent().length()!=2 ){
                sendInvalidInputError();
                System.out.println("!5 ||  !2");
                return;

            } else if(chatMessage.getContent().length()==5){

                if(!chatMessage.getContent().contains(",")){
                    sendInvalidInputError();
                    System.out.println("!,");
                    return;
                }

                response = chatMessage.getContent().split(",");

                // if left isnt 2 right also isnt
                if(response[0].length()!=2){
                    System.out.println("left not 2");
                    sendInvalidInputError();
                    return;
                }


            }else if(chatMessage.getContent().length()==2){
                response[0] = chatMessage.getContent();
            }


            if (game.isPlus2Played()) {
                if (response[1] != null) {
                    game.playRound(null, null, response, null, null);
                } else {
                    game.playRound(null, new String[game.getPlus2Stack()], response, new String[]{null, null, null}, null);
                }
            } else {
                if(chatMessage.getContent().contains("8")){
                    dealerSendMessagetoUser(game.getPlayers().get(game.getCurrPlayerIndex()).getUser().getName(), game.requestAction(1));
                    game.playRound(response[0], null, null, null, null);
                    game.setCurrPlayerIndex(game.getCurrPlayerIndex()-1);
                    waitingForResponse = true;
                    return;
                }else{
                    game.playRound(response[0], null, null, null, null);
                }
            }

            if(game.getCurrentTopCard().charAt(0) == 'A'){
                dealerBroadcastMessage(game.notifyAction(2));
            }else if(game.getCurrentTopCard().charAt(0) == 'Q'){
                dealerBroadcastMessage(game.notifyAction(3));
            }else if(game.getCurrentTopCard().charAt(0) == '8'){
                dealerBroadcastMessage(game.notifyAction(4));
            }
            sendCurrentCardAndTurn();
            sendPlayerHand();

            if (game.didReachWinningThreshold()) {
                dealerBroadcastMessage(game.endGame());
            }


        }else{
            for(Player p: players){
                if(chatMessage.getSender().equals(p.getName())){
                    dealerSendMessagetoUser("It is not your turn",p.getUser().getName());
                }
            }

        }
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public void newUser(@Payload final ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){

        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        players.add(new Player(headerAccessor.getUser(), chatMessage.getSender()));
        sendingOperations.convertAndSend("/topic/public",chatMessage);
        if(players.size()>=4){
            announceGameStart();

            game = new Crazy8Game(players);
            game.dealerDrawCard(null);
            game.dealPlayerCards(new String[][]{{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null}});

            sendCurrentCardAndTurn();
            sendPlayerHand();

            Player currentPlayer = game.getPlayers().get(game.getCurrPlayerIndex());

            if(!game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex()))){
                dealerSendMessagetoUser(currentPlayer.getUser().getName(),"player drew : " + game.drawUpTo3(new String[]{null,null,null}).toString());
                dealerSendMessagetoUser(currentPlayer.getUser().getName(),currentPlayer.getHand().toString());
            }
        }
    }
    public void announceGameStart(){
        final ChatMessage msg = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("Dealer")
                .content("4 Players Have Joined, Game Is Starting\n")
                .build();
        sendingOperations.convertAndSend("/topic/public",msg);
    }
    public void sendCurrentCardAndTurn(){
        final ChatMessage msg = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("Dealer")
                .content(game.showCardAndTurn())
                .build();
        sendingOperations.convertAndSend("/topic/public",msg);


    }
    public void dealerSendMessagetoUser(String content,String username){
        final ChatMessage msg = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("Dealer")
                .content(content)
                .build();
        sendingOperations.convertAndSendToUser(username,"/topic/private.messages", msg);
    }
    public void dealerBroadcastMessage(String content){
        final ChatMessage msg = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("Dealer")
                .content(content)
                .build();
        sendingOperations.convertAndSend("/topic/public", msg);
    }

    public void sendPlayerHand(){
        for(Player p: game.getPlayers()){
            final ChatMessage playerHand = ChatMessage.builder()
                    .type(MessageType.CHAT)
                    .sender("Dealer")
                    .content("Your hand : " + p.getHand().toString())
                    .build();
            sendingOperations.convertAndSendToUser(p.getUser().getName(), "/topic/private.messages",playerHand);
        }
    }

    public void sendInvalidInputError(){
        final ChatMessage msg = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("Dealer")
                .content("Invalid Response, write the ranks and the suits, Eg.'8H' or 'KH,3C'.")
                .build();
        sendingOperations.convertAndSendToUser(game.getPlayers().get(game.getCurrPlayerIndex()).getUser().getName(), "/topic/private.messages",msg);
    }
}
