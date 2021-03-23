package dev.donkz.pendragon.domain.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = Player.Builder.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class Player {
    private String id;
    private String username;
    private String profileIconUrl;
    private List<Player> friends;
    private List<Pc> characters;

    public static class Builder {
        @JsonProperty("id")
        private String id;

        @JsonProperty("username")
        private String username;

        @JsonProperty("profileIconUrl")
        private String profileIconUrl;

        @JsonProperty("friends")
        private List<Player> friends;

        @JsonProperty("characters")
        private List<Pc> characters;

        public Builder() {
            this.friends = new ArrayList<>();
            this.characters = new ArrayList<>();
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setProfileIconUrl(String profileIconUrl) {
            this.profileIconUrl = profileIconUrl;
            return this;
        }

        public Builder setFriends(List<Player> friends) {
            this.friends = friends;
            return this;
        }

        public Builder addFriend(Player friend) {
            this.friends.add(friend);
            return this;
        }

        public Builder setCharacters(List<Pc> characters) {
            this.characters = characters;
            return this;
        }

        public Builder addCharacter(Pc character) {
            this.characters.add(character);
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
