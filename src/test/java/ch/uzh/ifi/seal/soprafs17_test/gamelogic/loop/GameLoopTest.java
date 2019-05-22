package ch.uzh.ifi.seal.soprafs17.gamelogic.loop;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.gamelogic.*;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.ShipMoveFactory;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
        /*
        * created by pepe :)
        *
        * ___________████████__██████
        * _________█░░░░░░░░██_██░░░░░░█
        * ________█░░░░░░░░░░░█░░░░░░░░░█
        * _______█░░░░░░░███░░░█░░░░░░░░░█
        * _______█░░░░███░░░███░█░░░████░█
        * ______█░░░██░░░░░░░░███░██░░░░██
        * _____█░░░░░░░░░░░░░░░░░█░░░░░░░░███
        * ____█░░░░░░░░░░░░░██████░░░░░████░░█
        * ____█░░░░░░░░░█████░░░████░░██░░██░░█
        * ___██░░░░░░░███░░░░░░░░░░█░░░░░░░░███
        * __█░░░░░░░░░░░░░░█████████░░█████████
        * █░░░░░░░░░░█████_████████_█████_█
        * █░░░░░░░░░░█___█_████___███_█_█
        * █░░░░░░░░░░░░█_████_████__██_██████
        * ░░░░░░░░░░░░░█████████░░░████████░░░█
        * ░░░░░░░░░░░░░░░░█░░░░░█░░░░░░░░░░░░█
        * ░░░░░░░░░░░░░░░░░░░░██░░░░█░░░░░░██
        * ░░░░░░░░░░░░░░░░░░██░░░░░░░███████
        * ░░░░░░░░░░░░░░░░██░░░░░░░░░░█░░░░░█
        * ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
        * ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
        * ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
        * ░░░░░░░░░░░█████████░░░░░░░░░░░░░░██
        * ░░░░░░░░░░█▒▒▒▒▒▒▒▒███████████████▒▒█
        * ░░░░░░░░░█▒▒███████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█
        * ░░░░░░░░░█▒▒▒▒▒▒▒▒▒█████████████████
        * ░░░░░░░░░░████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█
        * ░░░░░░░░░░░░░░░░░░██████████████████
        * ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█
        * ██░░░░░░░░░░░░░░░░░░░░░░░░░░░██
        * ▓██░░░░░░░░░░░░░░░░░░░░░░░░██
        * ▓▓▓███░░░░░░░░░░░░░░░░░░░░█
        * ▓▓▓▓▓▓███░░░░░░░░░░░░░░░██
        * ▓▓▓▓▓▓▓▓▓███████████████▓▓█
        * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓██
        * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█
        *
        *
        *
        * */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class GameLoopTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Test
    public void twoPlayerGameInit() throws Exception {

        User u1 = new User("bob");
        User u2 = new User("pepe");
        Obelisk obelisk = new Obelisk(2);

        Game g = new Game();
        g.setNumPlayers(2);
        g.addPlayer(u1);
        g.addPlayer(u2);

        g.setUpGame();

        g.start();

        assertEquals( (Integer) 27,u1.player.sled.quarry.stonesLeft); //stonecount = 2
        assertEquals( (Integer) 26,u2.player.sled.quarry.stonesLeft); //stonecount = 3

        DepartingHarbour dh = (DepartingHarbour) g.siteBoards.get("DepartingHarbour");
        Integer size[] = {1,2,3,4};
        dh.setSizeOfShips(size);
        u1.player.decision = g.turnManager.createStandardDecision(u1.player);
        assertEquals(2, u1.player.decision.actions.size());


        assertNotNull(g.siteBoards.get("DepartingHarbour"));
        assertNotNull(((DepartingHarbour)g.siteBoards.get("DepartingHarbour")).ships);
        assertEquals(((DepartingHarbour)g.siteBoards.get("DepartingHarbour")).ships.size(),4);

        assertEquals(5, g.turnManager.actions.size());
        assertNotNull(g.turnManager.actions.get(0));

        ShipMoveFactory smf = (ShipMoveFactory) g.turnManager.actions.get(0).factory;


        assertNotNull( ((ShipMoveFactory) g.turnManager.actions.get(0).factory ).harbour );

        // dumme stei
        Move m = new Move();
        m.from = "Sled";
        m.to = "DepartingHarbour";
        m.pos = 0;
        m.ShipID = 3;
        m.setUser(u1);

        //Assert.assertTrue(smf.harbour.ships.get(0).setStone(0, PlayerFieldColour.BLACK));

        Assert.assertTrue(g.turnManager.executePlayerMove(m));



        //g.turnManager.setActiveUser(u1);
        assertEquals(g.turnManager.activePlayer, u2);

        assertEquals( (Integer) 27,u1.player.sled.quarry.stonesLeft);

        // it's player's 1 turn
        assertNull(u1.player.decision);
        assertNotNull(u2.player.decision);

        //assertEquals(2, u2.player.decision.actions.size()); // wayne


        Move mp1 = u2.player.decision.actions.get(1).moves.get(0);
        assertNotNull(mp1);
        assertEquals("Quarry", mp1.from);
        assertEquals("Sled", mp1.to);
        assertEquals( (Integer) 26,u2.player.sled.quarry.stonesLeft);
        assertEquals( (Integer) 3,u2.player.sled.sledLoad);


        //kinda shitty
        assertEquals((Integer) 2, mp1.pos); //mp1.pos gibt den wert der freien plätze als zahl zurück
        Player p = new Player(PlayerFieldColour.BLACK, 2);
        u2.player = p;


        dh = (DepartingHarbour) g.siteBoards.get("DepartingHarbour");
        assertEquals(4, dh.ships.size());
        assertNull( ((Obelisk)g.siteBoards.get("Obelisk")).ship );


        assertTrue(g.turnManager.executePlayerMove(mp1));

        Move nope = new Move("Market", "Market",99);

        Market market = (Market)g.siteBoards.get("Market");
        assertFalse(g.turnManager.executeAndBroadcast(m, market, market ));
/*        assertEquals(3, ((DepartingHarbour) g.siteBoards.get("DepartingHarbour")).ships.size());
        assertNotNull( ((Obelisk)g.siteBoards.get("Obelisk")).ship );
        */
    }


    @Test
    public void gimmeCardTest() throws JsonProcessingException {
        User u1 = new User();
        u1.setUsername("Pepe");
        User u2 = new User();
        u2.setUsername("Bob");

        Game g = new Game();
        g.setNumPlayers(2);
        g.addPlayer(u1);
        g.addPlayer(u2);

        g.setUpGame();

        g.start();

        DepartingHarbour dh = (DepartingHarbour) g.siteBoards.get("DepartingHarbour");
        Integer size[] = {1,2,3,4};
        dh.setSizeOfShips(size);
        u1.player.decision = g.turnManager.createStandardDecision(u1.player);
        assertEquals(2, u1.player.decision.actions.size());

        boolean foundsteilegge = false;
        Action stoneToShip = gimmeAction(u1.player.decision, "SledToShipAction");
        Assert.assertNotNull(stoneToShip);

        Move sledToShipMoveAkaSGipfel = null;

        for(Move gg : stoneToShip.moves) {
            if(dh.ships.get(gg.ShipID).numFields == 1)
                sledToShipMoveAkaSGipfel = gg;
        }
        Assert.assertNotNull(sledToShipMoveAkaSGipfel);

        sledToShipMoveAkaSGipfel.setUser(u1);


        Assert.assertFalse(dh.ships.get(0).movable());

        Assert.assertTrue(g.turnManager.executePlayerMove(sledToShipMoveAkaSGipfel));
        Assert.assertTrue(dh.ships.get(0).movable());

        assertNotNull(u2.player.decision);
        assertNull(u1.player.decision);
        assertEquals(3, u2.player.decision.actions.size());
        Action sailAction = gimmeAction(u2.player.decision, "SailAction");
        assertNotNull(sailAction);

       Move mandelgipfel = null;

        for(Move gg : sailAction.moves) {
            if(gg.to.equals("Market"))
                mandelgipfel = gg;
        }
        assertNotNull(mandelgipfel);

        mandelgipfel.setUser(u2);

        Assert.assertNotNull(mandelgipfel);
        Assert.assertTrue(g.turnManager.executePlayerMove(mandelgipfel));

        Action pickupCardAction = gimmeAction(u1.player.decision, "PickupCardAction");
        assertNotNull(pickupCardAction);


        assertEquals(4, pickupCardAction.moves.size());
        Move vanilleStange = pickupCardAction.moves.get(0);
        vanilleStange.setUser(u1);
        Assert.assertNotNull(u1);
        Assert.assertEquals("PlayerCardStack",vanilleStange.to );
        Assert.assertEquals("Market",vanilleStange.from );
        Assert.assertTrue(0<= vanilleStange.pos && vanilleStange.pos <= 50);

        Assert.assertNotNull(vanilleStange);
        Assert.assertTrue(g.turnManager.executePlayerMove(vanilleStange));



    }


    @Test
    public void twoPlayerGame() throws Exception {

        User u1 = userService.createUser("brudi");
        User u2 = userService.createUser("brudi2");

        Game g = gameService.createGame(u1.getToken(),2);
        Assert.assertNotNull(g);
        Assert.assertNotNull(g.getId());

        Assert.assertTrue(gameService.joinGame(g,u2));


    }

    private Action gimmeAction(Decision d, String name){
        for(Action a : d.actions){
            if(a.getClass().getSimpleName().equals(name)){
                return a;
            }
        }
        return null;
    }

}
