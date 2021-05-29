package dev.donkz.pendragon.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller for Management view
 */
public class ManagementController implements Initializable, ViewableController {
    @FXML private Pane campaignLayer;
    @FXML private Pane characterLayer;
    @FXML private StackPane contentPane;
    @FXML private Pane playerLayer;
    @FXML private ToggleButton tbtnCampaigns;
    @FXML private ToggleGroup toggleMenu;
    @FXML private Pane variantLayer;

    // Child Controllers
    @FXML private ViewableController campaignLayerController;
    @FXML private ViewableController characterLayerController;
    @FXML private ViewableController variantLayerController;

    private ViewableController parentController;
    private Pane currentPane;
    private final HashMap<String, Pane> panes;

    public ManagementController() {
        panes = new HashMap<>();
    }

    /**
     * Life-cycle method for JavaFX controllers
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPanes();
        initToggle();
        initVisibility();

        campaignLayerController.setParentController(this);
        characterLayerController.setParentController(this);
        variantLayerController.setParentController(this);
        // playerLayerController.setParentController(this);
    }

    /**
     * Load available content panes
     */
    private void loadPanes() {
        panes.put("campaigns", campaignLayer);
        panes.put("characters", characterLayer);
        panes.put("variants", variantLayer);
        panes.put("player", playerLayer);
    }

    /**
     * Initialize toggle group navigation for sidebar menu
     */
    private void initToggle() {
        toggleMenu.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            ToggleButton selected = (ToggleButton) observableValue.getValue();

            // Prevent users to deselect toggle button
            if (newToggle == null) {
                oldToggle.setSelected(true);
                return;
            }

            // Change content pane based on selected button
            if (selected != null) {
                String selectedId = selected.getId();
                String selectedPaneName = idToPaneName(selectedId);
                switchPane(selectedPaneName);
            }
        });
    }

    /**
     * Initialize content pane visibility of stack pane based on initially selected toggle.
     */
    private void initVisibility() {
        ToggleButton selected = (ToggleButton) toggleMenu.getSelectedToggle();

        panes.forEach((key, pane) -> {
            pane.setVisible(false);
        });

        if (selected != null) {
            String selectedId = selected.getId();
            currentPane = panes.get(idToPaneName(selectedId));
        } else {
            tbtnCampaigns.setSelected(true);
            currentPane = panes.get("campaigns");
        }
        currentPane.setVisible(true);
    }

    /**
     * Convert a toggle button ID to its correlating pane name
     *
     * @param id Toggle button id (sidebar menu)
     * @return pane name
     */
    private String idToPaneName(String id) {
        String paneName = "";
        if (id.equalsIgnoreCase("tbtnCampaigns")) {
            paneName = "campaigns";
        } else if (id.equalsIgnoreCase("tbtnCharacters")) {
            paneName = "characters";
        } else if (id.equalsIgnoreCase("tbtnVariants")) {
            paneName = "variants";
        } else if (id.equalsIgnoreCase("tbtnPlayer")) {
            paneName = "player";
        }
        return paneName;
    }

    /**
     * Switch pane based on given pane name
     *
     * @param paneName name of pane to switch to
     */
    public void switchPane(String paneName) {
        currentPane.setVisible(false);
        currentPane = panes.get(paneName);
        currentPane.setVisible(true);
        render();
    }

    @Override
    public ViewableController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(ViewableController parentController) {
        this.parentController = parentController;
    }

    public void switchView() {
    }

    @Override
    public void render() {
        if (campaignLayer.isVisible()) {
            campaignLayerController.render();
        } else if (characterLayer.isVisible()) {
            characterLayerController.render();
        } else if (variantLayer.isVisible()) {
            variantLayerController.render();
        }
    }
}
