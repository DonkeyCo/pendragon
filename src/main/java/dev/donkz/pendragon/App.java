package dev.donkz.pendragon;

import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import dev.donkz.pendragon.ui.MainWindow;
import javafx.application.Application;

public class App {
    public static void main(String[] args) throws RequiredAttributeMissingException, IndexAlreadyExistsException, MultiplePlayersException {
        System.out.println("Hello World");
        Application.launch(MainWindow.class, args);

//        CampaignRepository repo = new LocalCampaignRepository();
//        PlayerRepository pRepo = new LocalPlayerRepository();
//
//        Pc pc = new Pc();
//        pc.setId("ID");
//        pc.setName("Charaa");

//        Player pl = Player.builder().username("Donkey").character(pc).build();
//        pRepo.save(pl);

//        Player p = null;
//        try {
//            p = pRepo.findRegisteredPlayer();
//        } catch (MultiplePlayersException e) {
//            e.printStackTrace();
//        }
//
//        CampaignVariant variant = new CampaignVariant();
//        variant.setCreator(p);
//        variant.setId("ABCDDEF");
//        variant.setName("DnD 5e");
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
