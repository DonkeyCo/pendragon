package dev.donkz.pendragon.domain;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitCampaignTest {

    private Campaign campaign;
    private Player dm;

    @BeforeEach
    public void setUp() throws RequiredAttributeMissingException {
        dm = Player.builder().username("Tester").build();
        campaign = new Campaign("Campaign", null, dm);
    }

    @Test
    @DisplayName("Add a player who is not a DM")
    public void testAddPlayerNotDM() throws RequiredAttributeMissingException {
        Player player = Player.builder().username("User").build();
        campaign.addPlayer(player);

        assert campaign.getPlayers().contains(player);
    }

    @Test
    @DisplayName("Add the DM to campaign should fail")
    public void testAddPlayerIsDM() {
        campaign.addPlayer(dm);

        assert !campaign.getPlayers().contains(dm);
    }

    @Test
    @DisplayName("Add a character, who a participant created")
    public void testAddPcOfParticipant() throws RequiredAttributeMissingException {
        Player player = Player.builder().username("User").build();
        campaign.addPlayer(player);

        Pc pc = new Pc();
        pc.setId("TEST");
        pc.setName("TEST");
        player.addCharacter(pc);

        campaign.addCharacter(pc);
        assert campaign.getPcs().contains(pc);
    }

    @Test
    @DisplayName("Add a character, who was not created by a participant")
    public void testAddPcOfNoParticipant() throws RequiredAttributeMissingException {
        Player player = Player.builder().username("User").build();
        campaign.addPlayer(player);

        Pc pc = new Pc();
        pc.setId("TEST");
        pc.setName("TEST");

        campaign.addCharacter(pc);
        assert !campaign.getPcs().contains(pc);
    }

    @Test
    @DisplayName("Campaign should have ID")
    public void testCampaignCreation() {
        Campaign campaign = new Campaign();

        assert campaign.getId() != null;
    }

    @Test
    @DisplayName("Printable::shortString")
    public void testPrintableShortString() {
        Campaign campaign = new Campaign();
        campaign.setName("TEST");

        assert "TEST".equalsIgnoreCase(campaign.shortString());
    }

    @Test
    @DisplayName("Printable::longString")
    public void testLongString() {
        Campaign campaign = new Campaign();
        campaign.setName("TEST");

        assert "TEST: null | Campaign Variant: None | Dungeon Master: None | Players: None | Playable Characters: None".equalsIgnoreCase(campaign.longString());
    }
}
