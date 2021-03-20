package dev.donkz.pendragon.domain.campaign;

import dev.donkz.pendragon.domain.Repository;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;

import java.util.List;

public interface CampaignRepository extends Repository<Campaign> {
    Campaign findByName(String name);
    List<Campaign> findByDm(Player dm);
    List<Campaign> findByPlayer(Player player);
    List<Campaign> findByPc(Pc pc);
}
