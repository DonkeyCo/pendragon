package dev.donkz.pendragon.infrastructure.network.p2p.hive;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.util.JSONUtility;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.events.framework.interfaces.IFileEventListener;
import org.hive2hive.core.events.framework.interfaces.file.*;

import java.io.File;

@Listener(references = References.Strong)
public class HiveEventListener implements IFileEventListener {
    private final CampaignRepository campaignRepository;
    private final PcRepository pcRepository;
    private final PlayerRepository playerRepository;
    private final CampaignVariantRepository variantRepository;
    private final SessionRepository sessionRepository;

    private final IFileManager fileManager;
    private final JSONUtility jsonUtility;

    public HiveEventListener(IFileManager fileManager, CampaignRepository campaignRepository, PcRepository pcRepository, PlayerRepository playerRepository, CampaignVariantRepository variantRepository, SessionRepository sessionRepository) {
        this.fileManager = fileManager;
        this.jsonUtility = new JSONUtility();
        this.campaignRepository = campaignRepository;
        this.pcRepository = pcRepository;
        this.playerRepository = playerRepository;
        this.variantRepository = variantRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void onFileAdd(IFileAddEvent iFileAddEvent) {
        File file = iFileAddEvent.getFile();
        try {
            fileManager.createDownloadProcess(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String repositoryName = file.getName().split("_")[0];
        try {
            add(repositoryName, file);
        } catch (IndexAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileUpdate(IFileUpdateEvent iFileUpdateEvent) {
        File file = iFileUpdateEvent.getFile();
        try {
            fileManager.createDownloadProcess(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String repositoryName = file.getName().split("_")[0];
        String objectId = file.getName().split("_")[1];
        try {
            update(objectId, repositoryName, file);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileDelete(IFileDeleteEvent iFileDeleteEvent) {
        File file = iFileDeleteEvent.getFile();
        file.delete();

        String repositoryName = file.getName().split("_")[0];
        String objectId = file.getName().split("_")[1];
        try {
            delete(objectId, repositoryName);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileMove(IFileMoveEvent iFileMoveEvent) {
    }

    @Override
    public void onFileShare(IFileShareEvent iFileShareEvent) {
    }

    private void add(String repository, File file) throws IndexAlreadyExistsException {
        if (repository.equalsIgnoreCase("campaigns")) {
            Campaign campaign = jsonUtility.json2Object(file, Campaign.class);
            campaignRepository.save(campaign);
        } else if (repository.equalsIgnoreCase("campaignVariants")) {
            CampaignVariant variant = jsonUtility.json2Object(file, CampaignVariant.class);
            variantRepository.save(variant);
        } else if (repository.equalsIgnoreCase("pcs")) {
            Pc pc = jsonUtility.json2Object(file, Pc.class);
            pcRepository.save(pc);
        } else if (repository.equalsIgnoreCase("player")) {
            Player player = jsonUtility.json2Object(file, Player.class);
            playerRepository.save(player);
        } else if (repository.equalsIgnoreCase("session")) {
            Session session = jsonUtility.json2Object(file, Session.class);
            sessionRepository.save(session);
        }
    }

    private void update(String id, String repository, File file) throws EntityNotFoundException {
        if (repository.equalsIgnoreCase("campaigns")) {
            Campaign campaign = jsonUtility.json2Object(file, Campaign.class);
            campaignRepository.update(id, campaign);
        } else if (repository.equalsIgnoreCase("campaignVariants")) {
            CampaignVariant variant = jsonUtility.json2Object(file, CampaignVariant.class);
            variantRepository.update(id, variant);
        } else if (repository.equalsIgnoreCase("pcs")) {
            Pc pc = jsonUtility.json2Object(file, Pc.class);
            pcRepository.update(id, pc);
        } else if (repository.equalsIgnoreCase("player")) {
            Player player = jsonUtility.json2Object(file, Player.class);
            playerRepository.update(id, player);
        } else if (repository.equalsIgnoreCase("session")) {
            Session session = jsonUtility.json2Object(file, Session.class);
            sessionRepository.update(id, session);
        }
    }

    private void delete(String id, String repository) throws EntityNotFoundException {
        if (repository.equalsIgnoreCase("campaigns")) {
            campaignRepository.delete(id);
        } else if (repository.equalsIgnoreCase("campaignVariants")) {
            variantRepository.delete(id);
        } else if (repository.equalsIgnoreCase("pcs")) {
            pcRepository.delete(id);
        } else if (repository.equalsIgnoreCase("player")) {
            playerRepository.delete(id);
        } else if (repository.equalsIgnoreCase("session")) {
            sessionRepository.delete(id);
        }
    }
}
