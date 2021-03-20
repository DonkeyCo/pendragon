package dev.donkz.pendragon.infrastructure.persistence.local;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;

import java.util.ArrayList;
import java.util.List;

public class LocalCampaignRepository implements CampaignRepository {
    public static final String REPOSITORY = "campaigns";

    private final ObjectMapper mapper;
    private final FileHandler fileHandler;

    public LocalCampaignRepository() {
        this.mapper = new ObjectMapper();
        this.fileHandler = new FileHandler();
    }

    public Campaign findByName(String name) {
        return null;
    }

    public List<Campaign> findByDm(Player dm) {
        return null;
    }

    public List<Campaign> findByPlayer(Player player) {
        return null;
    }

    public List<Campaign> findByPc(Pc pc) {
        return null;
    }

    public void save(Campaign entity) {
        String content = null;
        try {
             content = mapper.writeValueAsString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (content != null) {
            fileHandler.save(REPOSITORY, content);
        }
    }

    public void delete(String id) {

    }

    public void update(String id, Campaign entity) {

    }

    public List<Campaign> findAll() {
        String content = fileHandler.read(REPOSITORY);
        List<Campaign> campaigns = new ArrayList<>();

        try {
            campaigns = mapper.readValue(content, new TypeReference<List<Campaign>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return campaigns;
    }

    public Campaign findById() {
        return null;
    }
}
