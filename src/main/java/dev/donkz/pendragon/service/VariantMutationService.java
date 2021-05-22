package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;

import javax.inject.Inject;

public class VariantMutationService {
    private final CampaignVariantRepository variantRepository;
    private final PlayerRepository playerRepository;

    @Inject
    public VariantMutationService(CampaignVariantRepository variantRepository, PlayerRepository playerRepository) {
        this.variantRepository = variantRepository;
        this.playerRepository = playerRepository;
    }

    public void createVariant(CampaignVariant variant, String name, String description, boolean isPublic) throws MultiplePlayersException, IndexAlreadyExistsException {
        variant.setCreator(playerRepository.findRegisteredPlayer());
        variant.setName(name);
        variant.setDescription(description);
        variant.setVisibility(isPublic);

        variantRepository.save(variant);
    }
}
