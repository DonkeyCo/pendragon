package dev.donkz.pendragon.service;

import com.google.inject.internal.asm.$RecordComponentVisitor;
import dev.donkz.pendragon.controller.ControllableSession;
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
import dev.donkz.pendragon.exception.infrastructure.*;
import dev.donkz.pendragon.infrastructure.network.Communicator;
import dev.donkz.pendragon.util.JSONUtility;
import javafx.application.Platform;

import javax.inject.Inject;
import java.util.List;

public class WebSocketSessionService {
    private final Communicator communicator;
    private final PlayerRepository playerRepository;
    private SessionRepository sessionRepository;
    private final JSONUtility jsonUtility;
    private ControllableSession controllableSession;
    private final CampaignVariantRepository variantRepository;
    private final CampaignRepository campaignRepository;
    private final PcRepository pcRepository;

    @Inject
    public WebSocketSessionService(Communicator communicator, PlayerRepository playerRepository, SessionRepository sessionRepository, CampaignVariantRepository variantRepository, CampaignRepository campaignRepository, PcRepository pcRepository) {
        this.communicator = communicator;
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
        this.jsonUtility = new JSONUtility();
        this.variantRepository = variantRepository;
        this.campaignRepository = campaignRepository;
        this.pcRepository = pcRepository;
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
            List<Session> sessions = sessionRepository.findAll();
            if (sessions.size() > 0) {
                Session session = sessions.get(0);
                session.setRoom((String) objects[0]);
                try {
                    sessionRepository.update(session.getId(), session);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> controllableSession.sync());
        });
        communicator.getSocket().on("joinedLobby", objects -> {
            System.out.println("Joined lobby");
            Session session = sessionRepository.findAll().get(0);
            Player joinedPlayer = jsonUtility.json2Object((String) objects[0], Player.class);
            session.addParticipant(joinedPlayer.getId(), null);
            try {
                sessionRepository.update(session.getId(), session);
                playerRepository.save(joinedPlayer);
            } catch (EntityNotFoundException | IndexAlreadyExistsException | SessionAlreadyExists e) {
                e.printStackTrace();
            }
            communicator.send("updateSession", session.getRoom(), jsonUtility.object2Json(session), jsonUtility.object2Json(player));
            Platform.runLater(() -> controllableSession.sync());
        });
        communicator.getSocket().on("leftLobby", objects -> {
            System.out.println("Left lobby");
            Session session = jsonUtility.json2Object((String) objects[1], Session.class);
            String leftPlayer = (String) objects[0];
            try {
                sessionRepository.saveOrUpdate(session);
            } catch (EntityNotFoundException | IndexAlreadyExistsException | SessionAlreadyExists e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                controllableSession.sync();
                controllableSession.left(leftPlayer);
            });
        });
        communicator.getSocket().on("sessionUpdated", objects -> {
            System.out.println("Session update");
            Session session = jsonUtility.json2Object((String) objects[0], Session.class);
            try {
                playerRepository.saveOrUpdate(jsonUtility.json2Object((String) objects[1], Player.class));
                sessionRepository.saveOrUpdate(session);
                campaignRepository.saveOrUpdate(session.getCampaign());
                variantRepository.saveOrUpdate(session.getCampaign().getCampaignVariant());
                for (Pc pc : session.getCampaign().getPcs()) {
                    pcRepository.saveOrUpdate(pc);
                }
            } catch (IndexAlreadyExistsException | SessionAlreadyExists | EntityNotFoundException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> controllableSession.sync());
        });
        communicator.getSocket().on("messageSent", objects -> {
            String playerName = (String) objects[0];
            String message =  (String) objects[1];

            Platform.runLater(() -> controllableSession.message(playerName, message));
        });
    }

    public void joinLobby(String channel) throws MultiplePlayersException {
        Player player = playerRepository.findRegisteredPlayer();

        communicator.send("joinLobby", channel, jsonUtility.object2Json(player));
        communicator.getSocket().on("sessionUpdated", objects -> {
            System.out.println("Session update");
            Session session = jsonUtility.json2Object((String) objects[0], Session.class);
            try {
                playerRepository.saveOrUpdate(jsonUtility.json2Object((String) objects[1], Player.class));
                sessionRepository.saveOrUpdate(session);
                campaignRepository.saveOrUpdate(session.getCampaign());
                variantRepository.saveOrUpdate(session.getCampaign().getCampaignVariant());
                for (Pc pc : session.getCampaign().getPcs()) {
                    pcRepository.saveOrUpdate(pc);
                }
            } catch (IndexAlreadyExistsException | SessionAlreadyExists | EntityNotFoundException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> controllableSession.sync());
        });
        communicator.getSocket().on("joinedLobby", objects -> {
            System.out.println("Joined lobby");
            Session session = sessionRepository.findAll().get(0);
            Player joinedPlayer = jsonUtility.json2Object((String) objects[0], Player.class);
            session.addParticipant(joinedPlayer.getId(), null);
            try {
                sessionRepository.saveOrUpdate(session);
                playerRepository.saveOrUpdate(joinedPlayer);
            } catch (EntityNotFoundException | IndexAlreadyExistsException | SessionAlreadyExists e) {
                e.printStackTrace();
            }
            communicator.send("updateSession", session.getRoom(), jsonUtility.object2Json(session), jsonUtility.object2Json(player));
            Platform.runLater(() -> controllableSession.sync());
        });
        communicator.getSocket().on("leftLobby", objects -> {
            System.out.println("Left lobby");
            Session session = jsonUtility.json2Object((String) objects[1], Session.class);
            String leftPlayer = (String) objects[0];
            try {
                sessionRepository.saveOrUpdate(session);
            } catch (EntityNotFoundException | IndexAlreadyExistsException | SessionAlreadyExists e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                controllableSession.sync();
                controllableSession.left(leftPlayer);
            });
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
        communicator.getSocket().on("messageSent", objects -> {
            String playerName = (String) objects[0];
            String message =  (String) objects[1];

            Platform.runLater(() -> controllableSession.message(playerName, message));
        });
    }

    public void leaveLobby(String room, String playerId, Session session) throws EntityNotFoundException {
        session.removeParticipant(playerRepository.findById(playerId).getId());
        communicator.send("leaveLobby", room, playerId, jsonUtility.object2Json(session));
        communicator.disconnect();
        sessionRepository.delete(session.getId());
    }

    public void updateSession(Session session) {
        Player player = null;
        try {
            player = playerRepository.findRegisteredPlayer();
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }
        communicator.send("updateSession", session.getRoom(), jsonUtility.object2Json(session), jsonUtility.object2Json(player));
    }

    public void setControllableSession(ControllableSession controllableSession) {
        this.controllableSession = controllableSession;
    }

    public void sendMessage(String message, Session session) {
        Player player = null;
        try {
            player = playerRepository.findRegisteredPlayer();
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }
        communicator.send("sendMessage", session.getRoom(), player.getUsername(), message);
    }
}
