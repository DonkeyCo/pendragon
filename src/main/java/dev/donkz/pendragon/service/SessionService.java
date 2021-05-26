package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;

import javax.inject.Inject;
import java.util.List;

public class SessionService {
    private SessionRepository sessionRepository;

    @Inject
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

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
}
