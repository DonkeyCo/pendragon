package dev.donkz.pendragon;

import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;
import dev.donkz.pendragon.infrastructure.network.socket.WebSocketCommunicator;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalSessionRepository;
import dev.donkz.pendragon.service.WebSocketSessionService;
import dev.donkz.pendragon.ui.MainWindow;
import javafx.application.Application;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World");
//        Application.launch(MainWindow.class, args);

        WebSocketSessionService sessionService = new WebSocketSessionService(
                new WebSocketCommunicator(),
                new LocalPlayerRepository(),
                new LocalSessionRepository(new LocalDriver())
        );
        sessionService.connect();
        sessionService.createLobby();

//        CampaignRepository repo = new LocalCampaignRepository();
//        PlayerRepository pRepo = new LocalPlayerRepository();
//        CampaignVariantRepository cvRepo = new LocalCampaignVariantRepository();
//        PcRepository pcRepo = new LocalPcRepository();
//
//        Pc pc = new Pc();
//        pc.setName("Character B");
//
//        WebSocketCommunication socket = new WebSocketCommunication();
//        socket.connect();
//
//        Session session = new Session();
//        socket.startLobby(session, pRepo.findRegisteredPlayer());
//        while (session.getChannelId() == null) {
//            Thread.sleep(1000);
//        }
//        System.out.println(session.getChannelId());
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
