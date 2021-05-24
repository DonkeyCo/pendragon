package dev.donkz.pendragon.domain.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.donkz.pendragon.domain.Entity;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = Player.PlayerBuilder.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@Builder
public class Player implements Entity {
    private String id;
    private String username;
    private String profileIconUrl;
    private List<Player> friends;
    private List<Pc> characters;

    public void addCharacter(Pc pc) {
        characters.add(pc);
    }

    public static class PlayerBuilder {
        @JsonProperty private String id;
        @JsonProperty private String username;
        @JsonProperty private String profileIconUrl;
        @JsonProperty private List<Player> friends;
        @JsonProperty private List<Pc> characters;

        public PlayerBuilder() {
            friends = new ArrayList<>();
            characters = new ArrayList<>();
        }

        public PlayerBuilder friend(Player player) {
            friends.add(player);
            return this;
        }

        public PlayerBuilder character(Pc pc) {
            characters.add(pc);
            return this;
        }

        public Player build() throws RequiredAttributeMissingException {
            if (this.username == null) {
                throw new RequiredAttributeMissingException("Username is required.");
            }
            if (this.id == null) {
                this.id = UUID.randomUUID().toString();
            }
            return new Player(id, username, profileIconUrl, friends, characters);
        }
    }
}
