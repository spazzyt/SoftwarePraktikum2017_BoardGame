package ch.uzh.ifi.seal.soprafs17.gamelogic.sites;
import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.gamelogic.*;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.CardMoveFactory;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.GreenMarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.ShipToken;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;

/**
 * Created by nairboon on 21.04.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MarketTest {

    public Integer size[] = {1, 2, 3, 4};

    private Game g;
    private User u1;
    private User u2;

    @Before
    public void setup() {
        u1 = new User("pepe");
        u2 = new User("bob");

        g = new Game();
        g.setNumPlayers(2);
        g.addPlayer(u1);
        g.addPlayer(u2);

        g.setUpGame();
        g.start();
    }


    @Test
    public void irgendwas() {

        Market market = (Market) g.siteBoards.get("Market");

        market.ship = new ShipToken(1, 0); // schiffskontrollcheckumgehung

        final int totalCards = 34;
        Assert.assertEquals(totalCards, market.allCards.size());
        Assert.assertEquals(totalCards - 4, market.availableDeck.size());
        Assert.assertEquals(4, market.pepeCards.size());

        //check if initCards does initialize red cards aswell
        for (int i = 18; i <= 23; i++) {
            Assert.assertEquals(market.allCards.get(i).id, (Integer) i);
            Assert.assertEquals(market.allCards.get(i).type, MarketCard.Type.Red);
        }

        //check if red card can be executed
        User u1 = new User("asdf");
        Player p1 = new Player(PlayerFieldColour.BLACK, 2);
        u1.player = p1;
        u1.player.marketCards.add(market.allCards.get(19));
        Move m = new Move();
        m.from = "PlayerCardStack";
        m.to = "Market";
        m.pos = 19;
        Assert.assertEquals(MarketCard.Type.Red, market.allCards.get(19).type);

        m.setUser(u1);
        Assert.assertTrue(market.executeMoveImplementation(m));

        market.newRound();
        Assert.assertEquals(4, market.pepeCards.size());
        Assert.assertEquals(totalCards - (2 * 4), market.availableDeck.size());

        CardMoveFactory cmf = new CardMoveFactory(market);

        List<Move> moves = cmf.createMoves(new Player());
        Assert.assertNotNull(moves);
        Assert.assertEquals(4, moves.size());

        for (Integer i = 0; i < 4; i++) {
            Move m2 = moves.get(i);
            Assert.assertEquals("PlayerCardStack", m2.to);
        }
    }

    @Test
    public void jarjarBinks() {
        Market market = (Market) g.siteBoards.get("Market");


        DepartingHarbour dh = (DepartingHarbour) g.siteBoards.get("DepartingHarbour");
        dh.setSizeOfShips(size);

        Move m = new Move();
        m.from = "DepartingHarbour";
        m.to = "Market";
        m.pos = 1;
        m.ship = dh.ships.get(1);
        m.toSite = market;


        Assert.assertTrue(m.ship.setStone(0, PlayerFieldColour.BLACK));
        Assert.assertFalse(m.ship.setStone(0, PlayerFieldColour.WHITE));
        Assert.assertTrue(m.ship.setStone(1, PlayerFieldColour.WHITE));

        Assert.assertEquals(0, market.colorStack.size());


        Assert.assertTrue(market.validateMove(m));
        Assert.assertTrue(dh.validateMove(m));

        Assert.assertTrue(market.executeMove(m));
        Assert.assertTrue(dh.executeMove(m));


        // on a size 2 ship only 2 can pick, duh
        Assert.assertEquals(2, market.colorStack.size());

        Assert.assertEquals(PlayerFieldColour.BLACK, market.colorStack.pop());
        Assert.assertEquals(PlayerFieldColour.WHITE, market.colorStack.pop());


    }

    @Test
    public void jarjarBinksTurnManager() throws JsonProcessingException {


        Assert.assertEquals(PlayerFieldColour.BLACK, u1.player.colour);
        Assert.assertEquals(PlayerFieldColour.WHITE, u2.player.colour);

        Market market = (Market) g.siteBoards.get("Market");
        DepartingHarbour dh = (DepartingHarbour) g.siteBoards.get("DepartingHarbour");
        dh.setSizeOfShips(size);

        Move m = new Move();
        m.from = "DepartingHarbour";
        m.to = "Market";
        m.pos = 1;


        Assert.assertTrue(dh.ships.get(1).setStone(0, PlayerFieldColour.BLACK));
        Assert.assertTrue(dh.ships.get(1).setStone(1, PlayerFieldColour.WHITE));

        Assert.assertEquals(0, market.colorStack.size());

        Assert.assertNotNull(u1.player.decision);
        Assert.assertNull(u2.player.decision);

        Assert.assertTrue(g.turnManager.executePlayerMove(m));
        //Assert.assertFalse(g.turnManager.executePlayerMove(m));

        // on a size 2 ship only 2 can pick, duh
        Assert.assertEquals(2 - 1, market.colorStack.size());

        Assert.assertNotNull(u1.player.decision);
        Assert.assertNull(u2.player.decision);

        // should be able to pick one market card
        Assert.assertEquals(4, u1.player.decision.actions.get(0).moves.size());


        // is Pick a card
        Move mcPick = u1.player.decision.actions.get(0).moves.get(0);
        mcPick.setUser(u1);
        Assert.assertNotNull(mcPick);

        Assert.assertEquals("Market", mcPick.from);
        Assert.assertEquals("PlayerCardStack", mcPick.to);


        Assert.assertEquals(0, ((Market) g.turnManager.siteBoards.get("Market")).playedDeck.size());
        Assert.assertEquals(4, ((Market) g.turnManager.siteBoards.get("Market")).pepeCards.size());
        Assert.assertEquals(30, ((Market) g.turnManager.siteBoards.get("Market")).availableDeck.size());
        //Assert.assertEquals(1, );

        Assert.assertEquals(1, market.colorStack.size());

        Assert.assertEquals(0, ((Market) g.turnManager.siteBoards.get("Market")).playedDeck.size());


        Assert.assertEquals(0, u1.player.marketCards.size());

        // Aufnahme
        Assert.assertTrue(g.turnManager.executePlayerMove(mcPick));

        Assert.assertEquals(0, market.colorStack.size());


        Assert.assertEquals(5, g.turnManager.actions.size());
//
        int mdSize;
        int pdSize;
        if (mcPick.pos < 18 || mcPick.pos > 23) { //is red
            pdSize = 0;
            mdSize = 1;
        } else {
            pdSize = 1;
            mdSize = 0;
        }
        Assert.assertEquals(pdSize, ((Market) g.turnManager.siteBoards.get("Market")).playedDeck.size());
        Assert.assertEquals(3, ((Market) g.turnManager.siteBoards.get("Market")).pepeCards.size());
        Assert.assertEquals(30, ((Market) g.turnManager.siteBoards.get("Market")).availableDeck.size());
        //Assert.assertEquals(6, ((Market)g.turnManager.siteBoards.get("Market"))..size());


        Assert.assertEquals(mdSize, u1.player.marketCards.size());


        Assert.assertNull(u1.player.decision);
        Assert.assertNotNull(u2.player.decision);


        // player 2 takes a card??
        Move takeit2 = u2.player.decision.actions.get(0).moves.get(0);
        Assert.assertNotNull(takeit2);

        takeit2.setUser(u2);
        Assert.assertEquals("Market", takeit2.from);
        Assert.assertEquals(3, u2.player.decision.actions.get(0).moves.size());

        Assert.assertTrue(g.turnManager.executePlayerMove(takeit2));

        //when red -> expected 0, when not red -> expected 1
        if (takeit2.pos < 18 || takeit2.pos > 23)
            Assert.assertEquals(1, u2.player.marketCards.size());
        else
            Assert.assertEquals(0, u2.player.marketCards.size());

        Assert.assertEquals(2, ((Market) g.turnManager.siteBoards.get("Market")).pepeCards.size());
        Assert.assertEquals(0, market.colorStack.size());


        Assert.assertNull(u1.player.decision);
        Assert.assertNotNull(u2.player.decision);



        /*
        Move playItV = u2.player.decision.actions.get(0).moves.get(0);
        Assert.assertNotNull(playItV);

        playItV.setUser(u2);
        Assert.assertEquals("Market", playItV.from);
        Assert.assertEquals(3, u2.player.decision.actions.get(0).moves.size());

        Assert.assertTrue(g.turnManager.executePlayerMove(playItV));

        Assert.assertEquals(1, u2.player.marketCards.size());
        */

    }


    @Test
    public void checkPointsStatue() {
        User u = new User();
        Player p = new Player(PlayerFieldColour.BLACK, 2);
        u.player = p;
        MarketCard.Type statue = MarketCard.Type.Statue;
        Market market = new Market();


        Move m1 = new Move();
        m1.from = "Market";
        m1.to = "PlayerCardStack";
        Assert.assertTrue(p.cardStack.validateMoveImplementation(m1));

        Move m2 = new Move();
        m2.from = "PlayerCardStack";
        m2.to = "Market";
        Assert.assertFalse(p.cardStack.validateMoveImplementation(m2));

        m2.pos = 0;
        m2.setUser(u);

        p.marketCards.add(new MarketCard(MarketCard.Type.Statue, 0));
        p.marketCards.add(new MarketCard(MarketCard.Type.Statue, 1));
        p.marketCards.add(new MarketCard(MarketCard.Type.Statue, 2));

        Assert.assertEquals(6, p.cardStack.assessPoints(p.colour));
    }


    @Test
    public void checkPointsGreenCard() {
        Temple temple = new Temple(2);
        GreenMarketCard greenMc = new GreenMarketCard(10, temple);


        temple.setStone(PlayerFieldColour.BLACK);
        Assert.assertEquals(0, temple.getGreenCount());
        temple.setStone(PlayerFieldColour.BLACK);
        temple.setStone(PlayerFieldColour.BLACK);

        //greenCard should give 1 point
        Assert.assertEquals(1, temple.getGreenCount());
        Assert.assertEquals(1, temple.getGreenCount());
        Assert.assertEquals(1, greenMc.gimmepoints());


        Obelisk obelisk = new Obelisk(2);
        GreenMarketCard greenMc2 = new GreenMarketCard(10, obelisk);


        obelisk.setStone(PlayerFieldColour.BLACK);
        Assert.assertEquals(0, obelisk.getGreenCount());
        obelisk.setStone(PlayerFieldColour.BLACK);
        obelisk.setStone(PlayerFieldColour.BLACK);

        //greenCard should give 1 point
        Assert.assertEquals(1, obelisk.getGreenCount());
        Assert.assertEquals(1, greenMc2.gimmepoints());

    }


    @Test
    public void checkMinor() {
        Market market = new Market();
        Assert.assertEquals(0, market.assessPoints(PlayerFieldColour.BLACK));
        market.setStone(PlayerFieldColour.BLACK);
        Assert.assertTrue(market.boo);



        /*
         if(availableDeck.size() < 4){
            availableDeck.addAll(playedDeck);
            playedDeck.clear();
            Collections.shuffle(availableDeck);
        }
        * */
        u1 = new User("pepeMC");
        u2 = new User("bobMC");

        Move falseMove = new Move();
        falseMove.from = "Market";
        falseMove.to = "PlayerCardStack";
        falseMove.pos = 111111;
        g = new Game();
        g.setNumPlayers(2);
        g.addPlayer(u1);
        g.addPlayer(u2);
        g.setUpGame();
        g.start();

        market = ((Market) g.turnManager.siteBoards.get("Market"));

        List<MarketCard> t1 = market.availableDeck.subList(3, 30);
        List<MarketCard> t2 = market.availableDeck.subList(0, 2);

        Assert.assertEquals(30, market.availableDeck.size());
        market.playedDeck = t1;
        market.availableDeck = t2;
        Assert.assertEquals(2, market.availableDeck.size());
        Assert.assertEquals(27, market.playedDeck.size());
        //market.newRound();
        //eventually test values after market.newRound();

        Assert.assertFalse(market.validateMoveImplementation(falseMove));

//        if(m.from.equals("Market") && m.to.equals("PlayerCardStack"))
        Move falseMove2 = new Move();
        falseMove2.from = "MarketS";
        falseMove2.to = "PlayerCardStackS";
        falseMove2.pos = 111111;
        Assert.assertFalse(market.validateMoveImplementation(falseMove2));



    }
}