package dev.donkz.pendragon.domain.session;

import dev.donkz.pendragon.domain.Entity;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.Player;
import lombok.Data;

import java.util.Map;

@Data
public class Session implements Entity {
    private String id;

    private Player host;
    private String room;
    private Map<String, String> participants;

    private Campaign campaign;

    public void addParticipant(String playerId, String pcId) {
        participants.put(playerId, pcId);
    }

    public void removeParticipant(String playerId) {
        participants.remove(playerId);
    }
}
