package dev.donkz.pendragon.domain.player;

import dev.donkz.pendragon.domain.character.Pc;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Player {
    private String id;

    private String username;
    private List<Player> friends;
    private List<Pc> characters;

    public Player() {
        this.id = UUID.randomUUID().toString();
    }
}
