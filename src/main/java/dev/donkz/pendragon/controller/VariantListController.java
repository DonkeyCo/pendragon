package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalCampaignVariantRepository;
import dev.donkz.pendragon.service.VariantListingService;
import dev.donkz.pendragon.ui.Tile;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class VariantListController {
    @FXML private TilePane tilePane;

    private final VariantListingService listingService;

    public VariantListController() {
        this.listingService = new VariantListingService(new LocalCampaignVariantRepository());
    }

    public void initialize() {
        createTiles();
    }

    private void createTiles() {
        List<CampaignVariant> variants = listingService.getAvailableVariants();
        for (CampaignVariant variant : variants) {
            SortedMap<String, String> items = getTileItems(variant);
            Tile tile = new Tile(variant.getName(), items);
            tilePane.getChildren().add(tile);
        }
    }

    /**
     * Creates the items map, which contains all Label/Value pairs to create a tile
     *
     * @param variant campaign variant
     * @return map of Label/Value pairs for tile
     */
    private SortedMap<String, String> getTileItems(CampaignVariant variant) {
        SortedMap<String, String> items = new TreeMap<>();
        items.put("Creator", variant.getCreator().getUsername());
        String visibility = variant.isVisibility() ? "PUBLIC" : "PRIVATE";
        items.put("Visibility", visibility);
        return items;
    }
}
