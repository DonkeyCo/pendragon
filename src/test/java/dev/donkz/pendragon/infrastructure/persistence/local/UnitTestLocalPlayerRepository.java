package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class UnitTestLocalPlayerRepository {
    private LocalDriver driver;
    private LocalPlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        driver = Mockito.mock(LocalDriver.class);
        this.playerRepository = new LocalPlayerRepository(driver);
    }

    @Test
    @DisplayName("Find By Name")
    public void testFindByName() throws RequiredAttributeMissingException {
        List<Player> players = new ArrayList<>();
        Player player = Player.builder().username("User").build();
        Player player1 = Player.builder().username("Username").build();
        players.add(player);
        players.add(player1);

        Mockito.when(driver.select("players", Player.class)).then(invocationOnMock -> players);

        List<Player> result = playerRepository.findByUsername("User");
        assert result.equals(players);
    }

    @Test
    @DisplayName("Find By Dungeon Master")
    public void testFindRegisteredPlayer() throws RequiredAttributeMissingException, MultiplePlayersException {
        Player player = Player.builder().username("User").build();

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        Mockito.when(driver.selectCustomIndex("players", "player", Player.class)).then(invocationOnMock -> players);

        Player result = playerRepository.findRegisteredPlayer();
        assert result.equals(player);
    }
}
