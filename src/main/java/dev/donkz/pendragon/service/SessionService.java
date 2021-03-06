package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;

import javax.inject.Inject;
import java.util.List;

/**
 * Service for Session Entity
 */
public class SessionService {
    private final SessionRepository sessionRepository;
    private final PlayerRepository playerRepository;
    private final PcRepository pcRepository;

    @Inject
    public SessionService(SessionRepository sessionRepository, PlayerRepository playerRepository, PcRepository pcRepository) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
        this.pcRepository = pcRepository;
    }

    /**
     * Get current session
     * @return
     */
    public Session getCurrentSession() {
        List<Session> sessions = sessionRepository.findAll();
        if (sessions.size() == 1) {
            return sessions.get(0);
        } else if (sessions.size() > 1) {
            // Delete every session to stop
            sessions.forEach(session -> {
                try {
                    sessionRepository.delete(session.getId());
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        return null;
    }

    /**
     * Clear sessions
     */
    public void clear() {
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            try {
                sessionRepository.delete(session.getId());
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update participants of a session
     * @param playerId
     * @param pcId
     * @throws EntityNotFoundException
     */
    public void updateParticipant(String playerId, String pcId) throws EntityNotFoundException {
        Player player = playerRepository.findById(playerId);

        if (player != null) {
            Session session = getCurrentSession();
            session.addParticipant(playerId, pcId);
            sessionRepository.update(session.getId(), session);
        }
    }

    /**
     * Update campaign of a session
     * @param campaign
     * @throws EntityNotFoundException
     */
    public void updateCampaign(Campaign campaign) throws EntityNotFoundException {
        Session session = getCurrentSession();
        session.setCampaign(campaign);

        sessionRepository.update(session.getId(), session);
    }
}
