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
import java.util.Arrays;
import java.util.List;


@Controller
public class ChatController {
    ArrayList<Player> players = new ArrayList<>();
    Crazy8Game game;

    @Autowired
    private SimpMessageSendingOperations sendingOperations;


    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public void sendMessage(@Payload final ChatMessage chatMessage){
        //test cases for part 1 and part 2
        if(chatMessage.getSender().equals(game.getPlayers().get(3).getName()) && chatMessage.getContent().equals("TEST_ROW_41")){
            game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3C")));
            game.setCurrentTopCard("4C");
            game.setCurrPlayerIndex(0);
            dealerSendMessagetoUser("Initialized Test Row 41",game.getPlayers().get(3).getUser().getName());

            sendCurrentCardAndTurn();
            sendPlayerHand(game.getPlayers().get(game.getCurrPlayerIndex()));
            return;
        }else if(chatMessage.getSender().equals(game.getPlayers().get(3).getName()) && chatMessage.getContent().equals("TEST_ROW_45")){
            game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3C")));
            game.setCurrentTopCard("4C");
            game.setCurrPlayerIndex(3);
            dealerSendMessagetoUser("Initialized Test Row 45",game.getPlayers().get(3).getUser().getName());

            sendCurrentCardAndTurn();
            sendPlayerHand(game.getPlayers().get(game.getCurrPlayerIndex()));
            return;
        }else if(chatMessage.getSender().equals(game.getPlayers().get(3).getName()) && chatMessage.getContent().equals("TEST_ROW_0")){
            game.getPlayers().get(0).setHand(new ArrayList<>(List.of("10C","3S")));
            game.getPlayers().get(1).setHand(new ArrayList<>(List.of("5C")));
            game.getPlayers().get(2).setHand(new ArrayList<>(List.of("6C")));
            game.getPlayers().get(3).setHand(new ArrayList<>(List.of("7C")));
            game.setCurrPlayerIndex(0);
            game.setCurrentTopCard("4C");
            dealerSendMessagetoUser("Initialized Test Row 0",game.getPlayers().get(3).getUser().getName());


            sendCurrentCardAndTurn();
            sendPlayerHand(game.getPlayers().get(game.getCurrPlayerIndex()));
            return;
        }




        if(chatMessage.getSender().equals(game.getPlayers().get(game.getCurrPlayerIndex()).getName())){

            //less than 0 DNE
            //more than 5 == invalid
            //accept only 2 or 5
            //reject everything else
            String[] response = new String[2];
            System.out.println(chatMessage.getContent().length());


            if(chatMessage.getContent().length()!=5 && chatMessage.getContent().length()!=2 & chatMessage.getContent().length()!=3){
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


            }else if(chatMessage.getContent().length()==2 || chatMessage.getContent().length()==3){
                response[0] = chatMessage.getContent();
                System.out.println(response[0]);
            }


            if (game.isPlus2Played()) {
                game.setPlus2Played(false);

            }

            Player currentPlayer = game.getPlayers().get(game.getCurrPlayerIndex());
            boolean result = game.playRound(response[0], null, null, null, null);
            dealerSendMessagetoUser("Player turn ended",currentPlayer.getUser().getName());

            if(result == false){
                dealerSendMessagetoUser("you cannot play that, try again", currentPlayer.getUser().getName());
                return;
            }


            if(game.getCurrentTopCard().charAt(0) == 'A'){
                dealerBroadcastMessage(game.notifyAction(2));
            }else if(game.getCurrentTopCard().charAt(0) == 'Q'){
                dealerBroadcastMessage(game.notifyAction(3));
            }else if(game.getCurrentTopCard().charAt(0) == '8'){
                dealerBroadcastMessage(game.notifyAction(4));
            }

            sendCurrentCardAndTurn();

            Player nextPlayer = game.getPlayers().get(game.getCurrPlayerIndex());
            sendPlayerHand(nextPlayer);

            System.out.println(nextPlayer.getUser().getName());
            if(!game.hasPlayableCard(nextPlayer)){

                String oldCard = game.getCurrentTopCard();
                dealerSendMessagetoUser("Player does not have a playable card, drawing cards",nextPlayer.getUser().getName());
                String[] drewcards = game.drawUpTo3(new String[]{null, null, null});
                dealerSendMessagetoUser("Player drew : " + Arrays.toString(drewcards),nextPlayer.getUser().getName());

                if(oldCard != game.getCurrentTopCard()){
                    dealerSendMessagetoUser("Player played : " + game.getCurrentTopCard(),nextPlayer.getUser().getName());
                    dealerSendMessagetoUser("Player turn ended",nextPlayer.getUser().getName());

                }else{
                    dealerSendMessagetoUser("Player drew 3 card and still can't play, turn over" ,nextPlayer.getUser().getName());
                }


                sendCurrentCardAndTurn();
                //next next player
                sendPlayerHand(game.getPlayers().get(game.getCurrPlayerIndex()));

            }

            if (game.didFinishRound()) {
                dealerBroadcastMessage(game.endGame());
                sendCurrentCardAndTurn();
                sendPlayerHand(game.getPlayers().get(game.getCurrPlayerIndex()));
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

            Player nextPlayer = game.getPlayers().get(game.getCurrPlayerIndex());
            sendPlayerHand(game.getPlayers().get(game.getCurrPlayerIndex()));


            if(!game.hasPlayableCard(nextPlayer)){

                String oldCard = game.getCurrentTopCard();
                dealerSendMessagetoUser("Player does not have a playable card, drawing cards",nextPlayer.getUser().getName());
                String[] drewcards = game.drawUpTo3(new String[]{null, null, null});
                dealerSendMessagetoUser("Player drew : " + Arrays.toString(drewcards),nextPlayer.getUser().getName());

                if(oldCard != game.getCurrentTopCard()){
                    dealerSendMessagetoUser("Player played : " + game.getCurrentTopCard(),nextPlayer.getUser().getName());
                    dealerSendMessagetoUser("Player turn ended",nextPlayer.getUser().getName());

                }else{
                    dealerSendMessagetoUser("Player drew 3 card and still can't play, turn over" ,nextPlayer.getUser().getName());
                }


                sendCurrentCardAndTurn();
                //next next player
                sendPlayerHand(game.getPlayers().get(game.getCurrPlayerIndex()));

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
        sendingOperations.convertAndSendToUser(game.getPlayers().get(game.getCurrPlayerIndex()).getUser().getName(),"/topic/private.messages",msg);


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

    public void sendPlayerHand(Player p){

        final ChatMessage playerHand = ChatMessage.builder()
                .type(MessageType.CHAT)
                .sender("Dealer")
                .content("Your hand : " + p.getHand().toString())
                .build();
        sendingOperations.convertAndSendToUser(p.getUser().getName(), "/topic/private.messages",playerHand);

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
