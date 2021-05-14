package dev.donkz.pendragon;

import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;

public class App {
    public static void main(String[] args) throws RequiredAttributeMissingException, IndexAlreadyExistsException, MultiplePlayersException {
        System.out.println("Hello World");
//
//        CampaignRepository repo = new LocalCampaignRepository();
//        PlayerRepository pRepo = new LocalPlayerRepository();
//
//        Pc pc = new Pc();
//        pc.setId("ID");
//        pc.setCharacterInformation(new CharacterInformation("Chara", "", "", "", "Kind", "Race"));
//
//        Player pl = Player.builder().username("Donkey").character(pc).build();
//        pRepo.save(pl);
//
//        Player p = null;
//        try {
//            p = pRepo.findRegisteredPlayer();
//        } catch (MultiplePlayersException e) {
//            e.printStackTrace();
//        }
//
//        CampaignVariant variant = new CampaignVariant();
//        variant.setCreator(p);
//        variant.setId("ABCDDE");
//        variant.setName("DnD 4e");
//
//        Campaign c = null;
//        try {
//            c = Campaign.builder().name("Campaign B").dm(pl).campaignVariant(variant).character(pc).player(pl).build();
//        } catch (RequiredAttributeMissingException e) {
//            e.printStackTrace();
//        }
//        try {
//            repo.save(c);
//        } catch (IndexAlreadyExistsException e) {
//            e.printStackTrace();
//        }
    }
}
