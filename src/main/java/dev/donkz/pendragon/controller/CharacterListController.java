package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.service.CharacterListingService;
import dev.donkz.pendragon.ui.Tile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class CharacterListController implements Initializable, Controller {
    @FXML
    private TilePane tilePane;

    private Controller parentController;
    private final CharacterListingService listingService;

    @Inject
    public CharacterListController(CharacterListingService listingService) {
        this.listingService = listingService;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTiles();
    }

    private void createTiles() {
        List<Pc> characters;
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
                Tile tile = new Tile(character.getId(), character.getName(), items);
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

    @Override
    public Controller getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    public void switchView() {
    }
}
