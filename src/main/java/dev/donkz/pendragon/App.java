package dev.donkz.pendragon;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalCampaignVariantRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPcRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.ui.MainWindow;
import javafx.application.Application;

public class App {
    public static void main(String[] args) throws RequiredAttributeMissingException, IndexAlreadyExistsException, MultiplePlayersException, EntityNotFoundException {
        System.out.println("Hello World");
        Application.launch(MainWindow.class, args);

//        CampaignRepository repo = new LocalCampaignRepository();
        PlayerRepository pRepo = new LocalPlayerRepository();
//        CampaignVariantRepository cvRepo = new LocalCampaignVariantRepository();
//        PcRepository pcRepo = new LocalPcRepository();
//
        Pc pc = new Pc();
        pc.setName("Character B");
//        pcRepo.save(pc);

//        Player pl = Player.builder().username("Donkey").character(pc).build();
//        pRepo.save(pl);

//        Player p = null;
//        try {
//            p = pRepo.findRegisteredPlayer();
//        } catch (MultiplePlayersException e) {
//            e.printStackTrace();
//        }
//        p.addCharacter(pc);
//
//        pRepo.update(p.getId(), p);
//
//        CampaignVariant variant = new CampaignVariant();
//        variant.setCreator(p);
//        variant.setId("ABCDDEF");
//        variant.setName("DnD 5e");
//        cvRepo.save(variant);

//
//        Campaign c = null;
//        c = new Campaign("Campaign B", variant, p);
//        c.addPlayer(p);
//        c.addCharacter(pc);
//        try {
//            repo.save(c);
//        } catch (IndexAlreadyExistsException e) {
//            e.printStackTrace();
//        }
    }
}
