package dev.donkz.pendragon.domain.session;

import dev.donkz.pendragon.domain.Entity;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import lombok.Data;

import java.util.Map;

@Data
public class Session implements Entity {
    private String id;

    private Player host;
    private String hostAddress;
    private Map<Player, Pc> participants;

    private Campaign campaign;

    public void addParticipant(Player player, Pc pc) {
        participants.put(player, pc);
    }
}
