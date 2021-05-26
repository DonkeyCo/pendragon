package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.exception.infrastructure.*;
import dev.donkz.pendragon.infrastructure.network.Communicator;
import dev.donkz.pendragon.util.JSONUtility;

import javax.inject.Inject;

public class WebSocketSessionService {
    private final Communicator communicator;
    private final PlayerRepository playerRepository;
    private SessionRepository sessionRepository;
    private final JSONUtility jsonUtility;

    @Inject
    public WebSocketSessionService(Communicator communicator, PlayerRepository playerRepository, SessionRepository sessionRepository) {
        this.communicator = communicator;
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
        this.jsonUtility = new JSONUtility();
    }

    public void connect() throws ConnectionException {
        boolean connected = communicator.connect();
        System.out.println(connected);
    }

    public void createLobby(Campaign campaign) throws MultiplePlayersException, IndexAlreadyExistsException, SessionAlreadyExists {
        Player player = playerRepository.findRegisteredPlayer();
        Session lobbySession = new Session();

        lobbySession.setId(player.getId());
        lobbySession.setHost(player);
        lobbySession.setCampaign(campaign);
        sessionRepository.save(lobbySession);

        communicator.send("createLobby", player.getId(), jsonUtility.object2Json(player));
        communicator.getSocket().on("createdLobby", objects -> {
            Session session = sessionRepository.findAll().get(0);
            session.setRoom((String) objects[0]);
            try {
                sessionRepository.update(session.getId(), session);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        });
        communicator.getSocket().on("joinedLobby", objects -> {
            Session session = sessionRepository.findAll().get(0);
            Player joinedPlayer = jsonUtility.json2Object((String) objects[0], Player.class);
            session.addParticipant(joinedPlayer.getId(), null);
            try {
                sessionRepository.update(session.getId(), session);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println(session.getRoom());
            communicator.send("updateSession", session.getRoom(), jsonUtility.object2Json(session));
        });
        communicator.getSocket().on("leftLobby", objects -> {
            Session session = jsonUtility.json2Object((String) objects[1], Session.class);
            try {
                sessionRepository.update(session.getId(), session);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

            communicator.send("updateSession", jsonUtility.object2Json(session));
        });
    }

    public void joinLobby(String channel) throws MultiplePlayersException {
        Player player = playerRepository.findRegisteredPlayer();

        communicator.send("joinLobby", channel, jsonUtility.object2Json(player));
        communicator.getSocket().on("sessionUpdated", objects -> {
            Session session = jsonUtility.json2Object((String) objects[0], Session.class);
            try {
                sessionRepository.save(session);
            } catch (IndexAlreadyExistsException | SessionAlreadyExists e) {
                e.printStackTrace();
            }
        });
        communicator.getSocket().on("hostLeft", objects -> {
            Session session = jsonUtility.json2Object((String) objects[1], Session.class);
            communicator.send("leaveLobby", session.getRoom(), player.getId(), jsonUtility.object2Json(session));
            communicator.disconnect();

            try {
                sessionRepository.delete(session.getId());
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void leaveLobby(String room, String playerId, Session session) throws EntityNotFoundException {
        session.removeParticipant(playerRepository.findById(playerId).getId());
        communicator.send("leaveLobby", room, playerId, jsonUtility.object2Json(session));
        communicator.disconnect();
        sessionRepository.delete(session.getId());
    }

    public void updateSession(Session session) {
        communicator.send("updateSession", session.getRoom(), jsonUtility.object2Json(session));
    }
}
