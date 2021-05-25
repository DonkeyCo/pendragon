package dev.donkz.pendragon.infrastructure.network.p2p.hive;

import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.infrastructure.network.p2p.Peer;
import dev.donkz.pendragon.util.FileHandler;
import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IFileConfiguration;
import org.hive2hive.core.api.interfaces.IH2HNode;
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

public class HivePeer implements Peer {
    private final IFileConfiguration fileConfig;
    private IH2HNode peerNode;
    private final IFileAgent fileAgent;
    private final FileHandler fileHandler;

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
        fileHandler = new FileHandler();
        this.campaignRepository = campaignRepository;
        this.pcRepository = pcRepository;
        this.playerRepository = playerRepository;
        this.variantRepository = variantRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void start(String nodeId) {
        peerNode = H2HNode.createNode(fileConfig);
        peerNode.connect(NetworkConfiguration.createInitial());
    }

    @Override
    public void connect(String host, String nodeId) throws UnknownHostException {
        peerNode = H2HNode.createNode(fileConfig);
        peerNode.connect(NetworkConfiguration.create(InetAddress.getByName(host)));
    }

    @Override
    public void register(String playerId) throws NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException {
        IUserManager userManager = peerNode.getUserManager();

        UserCredentials credentials = new UserCredentials("Alice", "password", "pin");

        userManager.createRegisterProcess(credentials).execute();
        System.out.println(playerId + " is registered: " + userManager.isRegistered("Alice"));
        userManager.createLoginProcess(credentials, fileAgent).execute();
        peerNode.getFileManager().subscribeFileEvents(new HiveEventListener(peerNode.getFileManager(), campaignRepository, pcRepository, playerRepository, variantRepository, sessionRepository));

        System.out.println(playerId + " is logged in: " + userManager.isLoggedIn());
    }

    @Override
    public void disconnect() {
        peerNode.disconnect();
    }

    @Override
    public void exchange(String content, String repository, String id) throws NoSessionException, NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException {
        Path path = Paths.get(fileAgent.getRoot().getPath(), repository + "_" + id + ".json");
        fileHandler.write2File(path, content);
        File file = new File(path.toUri());

        peerNode.getFileManager().createAddProcess(file).execute();
    }

    @Override
    public void remove(String repository, String id) throws NoSessionException, NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException {
        Path target = Paths.get(fileAgent.getRoot().getPath(), repository, repository + "_" + id + ".json");
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
