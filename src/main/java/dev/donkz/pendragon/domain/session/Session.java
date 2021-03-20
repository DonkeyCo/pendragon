package dev.donkz.pendragon.domain.session;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.Player;
import lombok.Data;

import java.util.List;

@Data
public class Session {
    private String id;

    private String name;
    private Player dm;  // host
    private List<Player> participants;
    private List<Player> spectators;

    private Campaign campaign;
}
