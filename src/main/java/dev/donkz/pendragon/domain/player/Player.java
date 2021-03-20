package dev.donkz.pendragon.domain.player;

import lombok.Data;

import java.util.List;

@Data
public class Player {
    private String username;
    private List<Player> friends;
    private List<Character> characters;
}
