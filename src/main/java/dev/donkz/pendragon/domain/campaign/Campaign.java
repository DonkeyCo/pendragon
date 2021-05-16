package dev.donkz.pendragon.domain.campaign;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class Campaign {
    @JsonProperty private String id;

    @JsonProperty private String name;
    @JsonProperty private String description;
    @JsonProperty private String notes;

    @JsonProperty private CampaignVariant campaignVariant;
    @JsonProperty private List<CampaignState> campaignStates;

    @JsonProperty private Player dm;
    @JsonProperty private List<Player> players;
    @JsonProperty private List<Pc> pcs;

    public Campaign() {
        this.id = UUID.randomUUID().toString();
        campaignStates = new ArrayList<>();
        players = new ArrayList<>();
        pcs = new ArrayList<>();
    }

    public Campaign(String name, CampaignVariant variant, Player dm) {
        this();
        this.name = name;
        this.campaignVariant = variant;
        this.dm = dm;
    }

    public void addState(CampaignState state) {
        campaignStates.add(state);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addCharacter(Pc pc) {
        pcs.add(pc);
    }
}
