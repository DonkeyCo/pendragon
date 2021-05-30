package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;

import javax.inject.Inject;

/**
 * CUD Operation Service for Campaign Variant Mutation
 */
public class VariantMutationService {
    private final CampaignVariantRepository variantRepository;
    private final PlayerRepository playerRepository;

    @Inject
    public VariantMutationService(CampaignVariantRepository variantRepository, PlayerRepository playerRepository) {
        this.variantRepository = variantRepository;
        this.playerRepository = playerRepository;
    }

    public void mutateVariant(CampaignVariant variant, String name, String description, boolean isPublic) throws MultiplePlayersException, EntityNotFoundException, IndexAlreadyExistsException, SessionAlreadyExists {
        boolean isNew = false;
        try {
            variantRepository.findById(variant.getId());
        } catch (EntityNotFoundException e) {
            isNew = true;
        }

        if (isNew) {
            variant.setCreator(playerRepository.findRegisteredPlayer());
            variant.setName(name);
            variant.setDescription(description);
            variant.setVisibility(isPublic);

            variantRepository.save(variant);
        } else {
            variant.setName(name);
            variant.setDescription(description);
            variant.setVisibility(isPublic);

            variantRepository.update(variant.getId(), variant);
        }
    }

    public void deleteVariant(String id) throws EntityNotFoundException {
        variantRepository.delete(id);
    }
}
