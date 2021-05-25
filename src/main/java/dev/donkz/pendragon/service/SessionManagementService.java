package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.infrastructure.network.p2p.Peer;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
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

    public Session createSession(Campaign campaign) throws NoPeerConnectionException, ProcessExecutionException, InvalidProcessStateException, UnknownHostException {
        Player player = playerManagementService.getRegisteredPlayer();

        String playerId = player.getId();
        peer.start(playerId);
        peer.register(playerId);

        Session session = new Session();
        session.setId(playerId);
        session.setHost(playerManagementService.getRegisteredPlayer());
        session.setCampaign(campaign);
        session.setHostAddress(InetAddress.getLocalHost().getHostAddress());

        return session;
    }

    public Session joinSession(String host) throws UnknownHostException, NoPeerConnectionException, ProcessExecutionException, InvalidProcessStateException {
        Player player = playerManagementService.getRegisteredPlayer();

        String playerId = player.getId();
        peer.connect(host, playerId);
        peer.register(playerId);

        return null;
    }
}
