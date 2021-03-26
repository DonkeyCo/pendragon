package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.character.Monster;
import dev.donkz.pendragon.domain.character.Npc;
import dev.donkz.pendragon.domain.player.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
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

    public CampaignVariant(String name, String description, boolean visibility, Player creator) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.creator = creator;
        this.spells = new ArrayList<>();
        this.features = new ArrayList<>();
        this.kinds = new ArrayList<>();
        this.proficiencies = new ArrayList<>();
        this.equipments = new ArrayList<>();
        this.traits = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.npcs = new ArrayList<>();
        this.monsters = new ArrayList<>();
    }
}
