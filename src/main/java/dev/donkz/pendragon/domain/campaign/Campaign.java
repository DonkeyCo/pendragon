package dev.donkz.pendragon.domain.campaign;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class Campaign {
    private String id;

    private String name;
    private String description;
    private String notes;

    private CampaignVariant campaignVariant;
    private List<CampaignState> campaignStates;

    private Player dm;
    private List<Player> players;
    private List<Pc> pcs;
}
