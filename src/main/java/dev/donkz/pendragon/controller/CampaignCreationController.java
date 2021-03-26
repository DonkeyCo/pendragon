package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalCampaignRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalCampaignVariantRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.service.CampaignCreationService;
import dev.donkz.pendragon.ui.Router;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.textfield.CustomTextField;

public class CampaignCreationController {
    private final Router router;
    private final CampaignCreationService creationService;

    @FXML private Pane root;
    @FXML private CustomTextField txtName;
    @FXML private CustomTextField txtDescription;
    @FXML private ComboBox<CampaignVariant> cmbVariant;
    @FXML private TextArea txtNotes;

    public CampaignCreationController(Router router) {
        this.router = router;
        this.creationService = new CampaignCreationService(new LocalCampaignRepository(), new LocalCampaignVariantRepository(), new LocalPlayerRepository());
    }

    @FXML
    public void initialize() {
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);

        setupVariants();
    }

    private void setupVariants() {
        ObservableList<CampaignVariant> variants = FXCollections.observableArrayList(creationService.getAvailableVariants());
        cmbVariant.setItems(variants);
        cmbVariant.setConverter(new StringConverter<>() {
            @Override
            public String toString(CampaignVariant campaignVariant) {
                if (campaignVariant != null) {
                    return campaignVariant.getName();
                }
                return "";
            }

            @Override
            public CampaignVariant fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    public void onSave(Event e) {
        if (txtName.getText().matches("\\s+")) {
            return;
        }
        if (cmbVariant.getValue() == null) {
            return;
        }
        try {
            creationService.createCampaign(txtName.getText(), txtDescription.getText(), cmbVariant.getValue(), txtNotes.getText());
        } catch (IndexAlreadyExistsException indexAlreadyExistsException) {
            indexAlreadyExistsException.printStackTrace();
        } catch (MultiplePlayersException multiplePlayersException) {
            multiplePlayersException.printStackTrace();
        }
        router.goToSub(HomeController.SUB_ROUTER, "campaigns");
    }
}
