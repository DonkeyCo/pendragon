package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.service.VariantListingService;
import dev.donkz.pendragon.service.VariantMutationService;
import dev.donkz.pendragon.ui.Tile;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import javax.inject.Inject;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Controller for Variant Listing
 */
public class VariantListController implements ViewableController {
    @FXML
    private TilePane tilePane;
    @FXML
    private Pane overview;
    @FXML
    private Pane editor;
    @FXML
    private VariantEditorController editorController;

    private ViewableController parentController;
    private final VariantListingService listingService;
    private final VariantMutationService mutationService;

    @Inject
    public VariantListController(VariantListingService listingService, VariantMutationService mutationService) {
        this.listingService = listingService;
        this.mutationService = mutationService;
    }

    public void initialize() {
        editorController.setParentController(this);
    }

    /**
     * Create Tiles for Campaign Variants
     */
    public void createTiles() {
        tilePane.getChildren().clear();
        List<CampaignVariant> variants = listingService.getAvailableVariants();
        for (CampaignVariant variant : variants) {
            SortedMap<String, String> items = getTileItems(variant);
            Tile tile = new Tile(variant.getId(), variant.getName(), items);

            tile.setOnMouseClicked(mouseEvent -> {
                editorController.setCampaignVariant(variant);
                editorController.render();
                this.switchView();
            });
            tile.getBtnDelete().setOnAction(actionEvent -> {
                try {
                    mutationService.deleteVariant(tile.getObjectId());
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
                this.createTiles();
            });

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

    @FXML
    public void onCreate() {
        switchView();
    }

    public void switchView() {
        overview.setVisible(!overview.isVisible());
        editor.setVisible(!editor.isVisible());
    }

    @Override
    public void render() {
        createTiles();
    }

    @Override
    public ViewableController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(ViewableController parentController) {
        this.parentController = parentController;
    }
}
