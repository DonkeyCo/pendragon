package dev.donkz.pendragon.infrastructure.network.p2p.hive;

import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.infrastructure.network.p2p.Peer;
import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IFileConfiguration;
import org.hive2hive.core.api.interfaces.IH2HNode;
import org.hive2hive.core.api.interfaces.INetworkConfiguration;
import org.hive2hive.core.api.interfaces.IUserManager;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.core.file.IFileAgent;
import org.hive2hive.core.security.UserCredentials;
import org.hive2hive.processframework.exceptions.InvalidProcessStateException;
import org.hive2hive.processframework.exceptions.ProcessExecutionException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class HivePeer implements Peer {
    private final IFileConfiguration fileConfig;
    private IH2HNode peerNode;
    private final IFileAgent fileAgent;

    private final CampaignRepository campaignRepository;
    private final PcRepository pcRepository;
    private final PlayerRepository playerRepository;
    private final CampaignVariantRepository variantRepository;
    private final SessionRepository sessionRepository;

    @Inject
    public HivePeer(CampaignRepository campaignRepository, PcRepository pcRepository, PlayerRepository playerRepository, CampaignVariantRepository variantRepository, SessionRepository sessionRepository) {
        this.fileConfig = FileConfiguration.createDefault();
        this.peerNode = null;
        fileAgent = new HiveFileAgent();
        this.campaignRepository = campaignRepository;
        this.pcRepository = pcRepository;
        this.playerRepository = playerRepository;
        this.variantRepository = variantRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void start(String nodeId) {
        INetworkConfiguration netConfig = NetworkConfiguration.createInitial(nodeId);

        if (peerNode != null) {
            // TODO: error handling
            System.out.println("Error: Node already exists.");
            return;
        }
        peerNode = H2HNode.createNode(fileConfig);
        peerNode.connect(netConfig);
    }

    @Override
    public void connect(String host, String nodeId) throws UnknownHostException {
        INetworkConfiguration netConfig = NetworkConfiguration.create(nodeId, InetAddress.getByName(host));

        if (peerNode != null) {
            // TODO: error handling
            System.out.println("Error: Node already exists.");
            return;
        }
        peerNode = H2HNode.createNode(fileConfig);
        peerNode.connect(netConfig);
    }

    @Override
    public void register(String playerId) throws NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException {
        IUserManager userManager = peerNode.getUserManager();

        UserCredentials credentials = new UserCredentials(playerId, "password", "pin");

        if (!userManager.isRegistered(credentials.getUserId())) {
            userManager.createRegisterProcess(credentials).execute();
        }
        System.out.println(playerId + " is registered: " + userManager.isRegistered(playerId));
        userManager.createLoginProcess(credentials, fileAgent).execute();
        peerNode.getFileManager().subscribeFileEvents(new HiveEventListener(peerNode.getFileManager(), campaignRepository, pcRepository, playerRepository, variantRepository, sessionRepository));

        System.out.println(playerId + " is logged in: " + userManager.isLoggedIn());
    }

    @Override
    public void disconnect() {
        peerNode.disconnect();
    }

    @Override
    public void exchange(String path, String repository, String id) throws NoSessionException, NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException {
        Path target = Paths.get(fileAgent.getRoot().getPath(), repository, repository + "_" + id);
        try {
            Files.copy(Paths.get(path), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Copy was not successful.");
            return;
        }
        File file = new File(target.toUri());
        peerNode.getFileManager().createAddProcess(file).execute();
    }

    @Override
    public void remove(String repository, String filename) throws NoSessionException, NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException {
        Path target = Paths.get(fileAgent.getRoot().getPath(), repository, filename);
        try {
            Files.delete(target);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Deletion was not successful");
        }
        File file = new File(target.toUri());
        peerNode.getFileManager().createDeleteProcess(file).execute();
    }
}
