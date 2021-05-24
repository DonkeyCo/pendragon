package dev.donkz.pendragon.domain.session;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultipleSessionsException;

import java.net.UnknownHostException;

public interface SessionService {
    void start(Campaign campaign, Player host) throws IndexAlreadyExistsException, UnknownHostException;
    void close() throws IndexAlreadyExistsException, EntityNotFoundException;
    void join(Player player, Pc pc) throws UnknownHostException, MultipleSessionsException;
    Session currentSession() throws MultipleSessionsException;
}
