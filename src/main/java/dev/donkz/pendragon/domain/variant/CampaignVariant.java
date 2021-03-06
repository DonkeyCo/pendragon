package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Entity;
import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.character.Monster;
import dev.donkz.pendragon.domain.character.Npc;
import dev.donkz.pendragon.domain.player.Player;
import javafx.scene.control.RadioButton;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CampaignVariant implements Printable, Entity {
    private String id;

    private String name;
    private String description;
    private boolean visibility;

    private Player creator;

    private List<Spell> spells; //
    private List<Feature> features; //
    private List<Kind> kinds; //
    private List<Proficiency> proficiencies; //
    private List<Equipment> equipments; //
    private List<Trait> traits; //
    private List<Skill> skills; //
    private List<Npc> npcs;
    private List<Monster> monsters;
    private List<Race> races; //

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
        this.races = new ArrayList<>();
    }

    public void addSpell(Spell spell) {
        spells.add(spell);
    }
    public void removeSpell(Spell spell) {
        spells.remove(spell);
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }
    public void removeFeature(Feature feature) {
        features.remove(feature);
    }

    public void addKind(Kind kind) {
        kinds.add(kind);
    }
    public void removeKind(Kind kind) {
        kinds.remove(kind);
    }

    public void addProficiency(Proficiency proficiency) {
        proficiencies.add(proficiency);
    }
    public void removeProficiency(Proficiency proficiency) {
        proficiencies.remove(proficiency);
    }

    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
    }
    public void removeEquipment(Equipment equipment) {
        equipments.remove(equipment);
    }

    public void addTrait(Trait trait) {
        traits.add(trait);
    }
    public void removeTrait(Trait trait) {
        traits.remove(trait);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    public void removeSkill(Skill skill) {
        skills.add(skill);
    }

    public void addNpc(Npc npc) {
        npcs.add(npc);
    }
    public void removeNpc(Npc npc) {
        npcs.remove(npc);
    }

    public void addMonsters(Monster monster) {
        monsters.add(monster);
    }
    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }

    public void addRace(Race race) {
        races.add(race);
    }
    public void removeRace(Race race) {
        races.remove(race);
    }

    @Override
    public String shortString() {
        return name;
    }

    public String longString() {
        return name + ": " + description + " | "
                + "Visibility: " + (visibility ? "Public" : "Private") + " | "
                + "Creator: " + creator.getUsername() + " | "
                + "Spells: " + spells.stream().map(Spell::getName).collect(Collectors.joining(",")) + " | "
                + "Features: " + features.stream().map(Feature::getName).collect(Collectors.joining(",")) + " | "
                + "Kinds: " + kinds.stream().map(Kind::getName).collect(Collectors.joining(",")) + " | "
                + "Races: " + races.stream().map(Race::getName).collect(Collectors.joining(",")) + " | "
                + "Proficiencies: " + proficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(",")) + " | "
                + "Equipment: " + equipments.stream().map(Equipment::getName).collect(Collectors.joining(",")) + " | "
                + "Traits: " + traits.stream().map(Trait::getName).collect(Collectors.joining(",")) + " | "
                + "Skills: " + skills.stream().map(Skill::getName).collect(Collectors.joining(",")) + " | "
                + "NPCs: " + npcs.stream().map(Npc::getName).collect(Collectors.joining(",")) + " | "
                + "Monsters: " + monsters.stream().map(Monster::getName).collect(Collectors.joining(","));
    }

    public String toString() {
        return name;
    }
}
