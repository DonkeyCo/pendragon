package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.character.Monster;
import dev.donkz.pendragon.domain.character.Npc;
import dev.donkz.pendragon.domain.player.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CampaignVariant {
    private String id;

    private String name;
    private String description;
    private boolean visibility;

    private Player creator;

    private List<Spell> spells;
    private List<Feature> features;
    private List<Kind> kinds;
    private List<Proficiency> proficiencies;
    private List<Equipment> equipments;
    private List<Trait> traits;
    private List<Skill> skills;
    private List<Npc> npcs;
    private List<Monster> monsters;
}
