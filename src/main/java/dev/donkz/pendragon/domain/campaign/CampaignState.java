package dev.donkz.pendragon.domain.campaign;

import lombok.Value;

import java.util.HashMap;

@Value
public class CampaignState {
    String name;

    Map map;
    HashMap<String, Position> pcPositions;
    HashMap<String, Position> npcPositions;
    HashMap<String, Position> monsterPositions;
}
