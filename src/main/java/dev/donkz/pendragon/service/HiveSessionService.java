package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.*;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultipleSessionsException;

import javax.inject.Inject;
import java.net.UnknownHostException;
import java.util.List;

public class HiveSessionService implements SessionService {
    private final SessionRepository sessionRepository;

    @Inject
    public HiveSessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void start(Campaign campaign, Player host) throws IndexAlreadyExistsException, UnknownHostException {
        Session session = new Session();
        session.setId(host.getId());
        session.setCampaign(campaign);
        session.setHost(host);

        sessionRepository.save(session);
    }

    /**
     * Closes the session. Preemptively closes every existing session to prevent possible multiple sessions.
     *
     * @throws EntityNotFoundException - session was not found
     */
    @Override
    public void close() throws EntityNotFoundException {
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            sessionRepository.delete(session.getId());
        }
    }

    @Override
    public void join(Player player, Pc pc) throws UnknownHostException, MultipleSessionsException {
        currentSession().addParticipant(player, pc);
    }

    @Override
    public Session currentSession() throws MultipleSessionsException {
        List<Session> sessions = sessionRepository.findAll();
        if (sessions.size() == 0) {
            return null;
        } else if (sessions.size() > 1) {
            throw new MultipleSessionsException();
        }
        return sessions.get(0);
    }
}
