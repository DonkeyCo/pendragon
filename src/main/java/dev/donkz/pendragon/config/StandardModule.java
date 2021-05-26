package dev.donkz.pendragon.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.infrastructure.database.local.Driver;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;
import dev.donkz.pendragon.infrastructure.network.Communicator;
import dev.donkz.pendragon.infrastructure.network.socket.WebSocketCommunicator;
import dev.donkz.pendragon.infrastructure.persistence.local.*;
import dev.donkz.pendragon.service.*;

public class StandardModule extends AbstractModule {
    @Provides
    static CampaignRepository campaignRepository() {
        return new LocalCampaignRepository();
    }

    @Provides
    static CampaignVariantRepository campaignVariantRepository() {
        return new LocalCampaignVariantRepository();
    }

    @Provides
    static PcRepository pcRepository() {
        return new LocalPcRepository();
    }

    @Provides
    static PlayerRepository playerRepository() {
        return new LocalPlayerRepository();
    }

    @Provides
    static CampaignManipulationService campaignCreationService() {
        return new CampaignManipulationService(campaignRepository(), campaignVariantRepository(), playerRepository());
    }

    @Provides
    static CampaignListingService campaignListingService() {
        return new CampaignListingService(campaignRepository());
    }

    @Provides
    static CharacterListingService characterListingService() {
        return new CharacterListingService(pcRepository(), playerRepository());
    }

    @Provides
    static PlayerManagementService playerManagementService() {
        return new PlayerManagementService(playerRepository());
    }

    @Provides
    static VariantListingService variantListingService() {
        return new VariantListingService(campaignVariantRepository());
    }

    @Provides
    static SessionRepository sessionRepository() {
        return new LocalSessionRepository(driver());
    }

    @Provides
    static Driver driver() {
        return new LocalDriver();
    }

    @Provides
    static Communicator communicator() {
        return new WebSocketCommunicator();
    }

}
