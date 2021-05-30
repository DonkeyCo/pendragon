package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class UnitTestLocalCampaignRepository {

    private LocalDriver driver;
    private LocalCampaignRepository campaignRepository;

    @BeforeEach
    public void setUp() {
        driver = Mockito.mock(LocalDriver.class);
        this.campaignRepository = new LocalCampaignRepository(driver);
    }

    @Test
    @DisplayName("Find By Name")
    public void testFindByName() {
        List<Campaign> campaigns = new ArrayList<>();
        Campaign campaign = new Campaign();
        campaign.setName("");
        campaigns.add(campaign);

        Mockito.when(driver.select("campaigns", Campaign.class)).then(invocationOnMock -> campaigns);

        List<Campaign> result = campaignRepository.findByName("");
        assert result.equals(campaigns);
    }

    @Test
    @DisplayName("Find By Dungeon Master")
    public void testFindByDm() throws RequiredAttributeMissingException {
        List<Campaign> campaigns = new ArrayList<>();
        Campaign campaign = new Campaign();
        Player player = Player.builder().username("User").build();
        campaign.setDm(player);
        campaigns.add(campaign);

        Mockito.when(driver.select("campaigns", Campaign.class)).then(invocationOnMock -> campaigns);

        List<Campaign> result = campaignRepository.findByDm(player);
        assert result.equals(campaigns);
    }

    @Test
    @DisplayName("Find By Player")
    public void testFindByPlayer() throws RequiredAttributeMissingException {
        List<Campaign> campaigns = new ArrayList<>();
        Campaign campaign = new Campaign();
        Player player = Player.builder().username("User").build();
        Player player2 = Player.builder().username("User2").build();
        campaign.setDm(player2);
        campaign.addPlayer(player);
        campaigns.add(campaign);

        Mockito.when(driver.select("campaigns", Campaign.class)).then(invocationOnMock -> campaigns);

        List<Campaign> result = campaignRepository.findByPlayer(player);
        assert result.equals(campaigns);
    }

    @Test
    @DisplayName("Find By Pc")
    public void testFindByPc() throws RequiredAttributeMissingException {
        List<Campaign> campaigns = new ArrayList<>();
        Campaign campaign = new Campaign();
        Player player = Player.builder().username("TEST").build();
        Player player2 = Player.builder().username("TEST2").build();
        Pc pc = new Pc();
        pc.setName("");
        player.addCharacter(pc);
        campaign.setDm(player2);
        campaign.addPlayer(player);
        campaign.addCharacter(pc);
        campaigns.add(campaign);

        Mockito.when(driver.select("campaigns", Campaign.class)).then(invocationOnMock -> campaigns);

        List<Campaign> result = campaignRepository.findByPc(pc);
        assert result.equals(campaigns);
    }

    @Test
    @DisplayName("Find All")
    public void testFindAll() {
        List<Campaign> campaigns = new ArrayList<>();
        Campaign campaign = new Campaign();
        Campaign campaign2 = new Campaign();
        Campaign campaign3 = new Campaign();
        campaigns.add(campaign);
        campaigns.add(campaign2);
        campaigns.add(campaign3);

        Mockito.when(driver.select("campaigns", Campaign.class)).then(invocationOnMock -> campaigns);

        List<Campaign> result = campaignRepository.findAll();
        assert result.equals(campaigns);
    }
}
