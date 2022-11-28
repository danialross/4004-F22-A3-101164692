package com.example.A3.model;

import java.security.Principal;
import java.util.ArrayList;

public class Player {
    private Principal user;
    private String name;
    private ArrayList<String> hand;

    public Player(Principal user, String name){
        this.user = user;
        this.name = name;
    }

    public Principal getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

}
