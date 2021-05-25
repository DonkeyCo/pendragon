package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.Entity;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.infrastructure.network.p2p.Peer;
import dev.donkz.pendragon.infrastructure.network.p2p.hive.ShareRequest;
import dev.donkz.pendragon.util.JSONUtility;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.processframework.exceptions.InvalidProcessStateException;
import org.hive2hive.processframework.exceptions.ProcessExecutionException;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SessionManagementService {

    private final Peer peer;
    private final PlayerManagementService playerManagementService;

    @Inject
    public SessionManagementService(Peer peer, PlayerManagementService playerManagementService) {
        this.peer = peer;
        this.playerManagementService = playerManagementService;
    }

    public Session createSession(Campaign campaign) throws NoPeerConnectionException, ProcessExecutionException, InvalidProcessStateException, UnknownHostException, NoSessionException {
        Player player = playerManagementService.getRegisteredPlayer();

        String playerId = player.getId();
        peer.start(playerId);
        peer.register(playerId);

        Session session = new Session();
        session.setId(playerId);
        session.setHost(playerManagementService.getRegisteredPlayer());
        session.setCampaign(campaign);
        session.setHostAddress(InetAddress.getLocalHost().getHostAddress());

        // exchangeEntity(session, Session.class.getName(), session.getId());
        // exchangeEntity(campaign, Campaign.class.getName(), campaign.getId());
        // exchangeEntity(player, Player.class.getName(), player.getId());

        return session;
    }

    public Session joinSession(String host) throws UnknownHostException, NoPeerConnectionException, ProcessExecutionException, InvalidProcessStateException, NoSessionException {
        Player player = playerManagementService.getRegisteredPlayer();

        String playerId = player.getId();
        peer.connect(host, playerId);
        peer.register(playerId);

        ShareRequest request = new ShareRequest(playerId);
        exchangeEntity(new ShareRequest(playerId), "shareRequests", request.getId());

        return null;
    }

    public void exchangeEntity(Object object, String entityType, String id) throws NoPeerConnectionException, NoSessionException, ProcessExecutionException, InvalidProcessStateException {
        JSONUtility jsonUtility = new JSONUtility();
        String content = jsonUtility.object2Json(object);

        peer.exchange(content, getRepository(entityType), id);
    }

    private String getRepository(String entityType) {
        if (entityType.equalsIgnoreCase("Session")) {
            return "session";
        } else if (entityType.equalsIgnoreCase("Campaign")) {
            return "campaigns";
        } else if (entityType.equalsIgnoreCase("CampaignVariant")) {
            return "campaignVariants";
        } else if (entityType.equalsIgnoreCase("Pc")) {
            return "pcs";
        } else if (entityType.equalsIgnoreCase("Player")) {
            return "players";
        }
        return entityType;
    }
}
