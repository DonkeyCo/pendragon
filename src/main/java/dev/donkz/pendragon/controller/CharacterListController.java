package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPcRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.service.CharacterListingService;
import dev.donkz.pendragon.ui.Tile;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CharacterListController {
    @FXML private TilePane tilePane;

    private final CharacterListingService listingService;

    public CharacterListController() {
        this.listingService = new CharacterListingService(new LocalPcRepository(), new LocalPlayerRepository());
    }

    public void initialize() {
        createTiles();
    }

    private void createTiles() {
        List<Pc> characters = new ArrayList<>();
        try {
            characters = listingService.getPlayerCharacters();
        } catch (MultiplePlayersException e) {
            // TODO: Exception handling
            e.printStackTrace();
            return;
        }

        if (characters.size() > 0) {
            for (Pc character : characters) {
                SortedMap<String, String> items = getTileItems(character);
                Tile tile = new Tile(character.getName(), items);
                tilePane.getChildren().add(tile);
            }
        }
    }

    /**
     * Creates the items map, which contains all Label/Value pairs to create a tile
     *
     * @param pc character
     * @return map of Label/Value pairs for tile
     */
    private SortedMap<String, String> getTileItems(Pc pc) {
        SortedMap<String, String> items = new TreeMap<>();
        items.put("Class", pc.getKind());
        items.put("Race", pc.getRace());
        items.put("Level", String.valueOf(pc.getLevel()));
        return items;
    }
}
