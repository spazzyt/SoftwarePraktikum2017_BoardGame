package ch.uzh.ifi.seal.soprafs17.gamelogic.cards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.*;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SailAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SledToShipAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.MoveFactory;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.BlueMarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static junit.framework.TestCase.fail;

/**
 * Created by nairboon on 01.05.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class BlueMarketCardTest {
    private Integer size[] = {1,2,3,4};


    private User u1;
    private User u2;
    private Game g;
    private SailAction sa;
    private SledToShipAction ssa;

    @Before
    public void setup() {

        u1 = new User("pepeMC");
        u2 = new User("bobMC");

        g = new Game();
        g.setNumPlayers(2);
        g.addPlayer(u1);
        g.addPlayer(u2);
        g.setUpGame();
        g.start();


        for(Action a : g.turnManager.actions) {
            if (a.type == Action.Type.Sail) {
                sa = (SailAction) a;
            } else if (a.type == Action.Type.Sled) {
                ssa = (SledToShipAction) a;
            }
        }

    }


    @Test
    public void blueCardInit() {

        try {
            BlueMarketCard bmc = new BlueMarketCard(BlueMarketCard.BlueType.Hammer, 1, null, null);
            fail("Method didn't throw");
        } catch(Exception e) {}
    }



    @Test
    public void hammerTest() throws Exception {

        BlueMarketCard bmc = new BlueMarketCard(BlueMarketCard.BlueType.Hammer, 1, sa, ssa);
        bmc.market =  (Market) g.siteBoards.get("Market");

        // lame af
        Assert.assertEquals(1, bmc.gimmepoints);
        Assert.assertEquals(MarketCard.Type.Blue, bmc.type);

        // p.quarry.stonesLeft >= 3 && p.sled.sledLoad < 3
        Player p2 = new Player(PlayerFieldColour.BLACK, 2);
        Player p3 = new Player(PlayerFieldColour.WHITE, 3);
        Player p4 = new Player(PlayerFieldColour.BROWN, 4);
        Player p5 = new Player(PlayerFieldColour.GREY,  5);

        p5.sled.sledLoad = 5;
        Assert.assertFalse(bmc.isPlayable(p5));
        //false because stonesLeft is not 3 or bigger
        p5.quarry.stonesLeft = 2;
        Assert.assertFalse(bmc.isPlayable(p5));
        //false because sledLoad
        p5.sled.sledLoad = 1;
        Assert.assertFalse(bmc.isPlayable(p5));

        //should give false first with p.quarry.stonesLeft < 3
        Assert.assertFalse(bmc.isPlayable(p3));
        //should give false first with p.sled.sledLoad >= 3
        Assert.assertFalse(bmc.isPlayable(p4));

        //should give true
        Assert.assertTrue(bmc.isPlayable(p2));

        /* what?
        Move m = new Move();
        m.setUser(u1);
        u1.player = p2;
        Assert.assertTrue(bmc.playCard(m));


        Assert.assertNotNull(bmc.moves);
        Assert.assertTrue(8 <= bmc.moves.size());
        Assert.assertTrue(13 >= bmc.moves.size());
        */


    }




    @Test
    public void LeverTest() throws Exception {

        BlueMarketCard bmc = new BlueMarketCard(BlueMarketCard.BlueType.Lever, 1, sa, ssa);

        // there are no ships so card is not playable
        Assert.assertFalse(bmc.isPlayable(u1.player));

        DepartingHarbour harbour = (DepartingHarbour) g.siteBoards.get("DepartingHarbour");
        Integer size[] = {1,2,3,4};
        harbour.setSizeOfShips(size);

        int ship = 1;

        // ships should be sailable
        Assert.assertTrue(harbour.ships.get(ship).setStone(0, PlayerFieldColour.BLACK));
        Assert.assertTrue(harbour.ships.get(ship).movable());

        u1.player.decision = g.turnManager.createStandardDecision(u1.player);
        Assert.assertNotNull(u1.player.decision);


        // since there is a sailable ship, the card should be playable
        Assert.assertTrue(bmc.isPlayable(u1.player));


        // --------

        bmc = getCard(BlueMarketCard.BlueType.Lever, (Market) g.siteBoards.get("Market"));
        bmc.market =  (Market) g.siteBoards.get("Market");

        u1.player.marketCards.add(bmc);

        Move m = new Move("PlayerCardStack", "Market", bmc.id);
        m.setUser(u1);


        Assert.assertTrue(g.turnManager.executePlayerMove(m));


        // step 1
        m = getFirstMove(u1);

        // which ship did we pick
        ship = m.pos;

        Assert.assertEquals("DepartingHarbour",m.from);
        Assert.assertEquals("DepartingHarbour",m.to);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));


        // check that the ships have a new order
        Assert.assertEquals(PlayerFieldColour.values()[m.LeverStones.get(0)].ordinal() , harbour.ships.get(ship).fields[0]);
        Assert.assertEquals(PlayerFieldColour.values()[m.LeverStones.get(1)].ordinal() , harbour.ships.get(ship).fields[1]);


        // step 2, sail it
        m = getFirstMove(u1);
        Assert.assertEquals("DepartingHarbour",m.from);
        Assert.assertNotEquals("DepartingHarbour",m.to);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));

    }

    /*
     When you get this market card, it remains
    face up in front of you until you use it. In a
    later turn, you can perform the following
    action a single time:
    Place 2 stones on 1 ship or 1 stone on each of 2 ships.
    After play, the card is placed on the discard pile.
    If you have not used the card by the end of the game, you
    will get 1 point for it.

     */
    @Test
    public void chiselTest() throws Exception {
        BlueMarketCard bmc = new BlueMarketCard(BlueMarketCard.BlueType.Chisel, 1, sa, ssa);


    }


    /*
    When you get this market card, it remains
    face up in front of you until you use it. In a
    later turn, you can perform the following
    action a single time:
    Place 1 stone on 1 ship and sail this ship to a site.
    As always, of course, both conditions for sailing a ship
    must have been met.
    After play, the card is placed on the discard pile.
    If you have not used the card by the end of the game, you
    will get 1 point for it.
     */
    @Test
    public void sailTest() throws Exception {

        Assert.assertEquals((Integer)2, u1.player.sled.sledLoad);

        BlueMarketCard bmc = new BlueMarketCard(BlueMarketCard.BlueType.Sail, 1, sa, ssa);

        DepartingHarbour harbour = (DepartingHarbour) g.siteBoards.get("DepartingHarbour");
        Integer size[] = {1,2,3,4};
        harbour.setSizeOfShips(size);


        bmc = getCard(BlueMarketCard.BlueType.Sail, (Market) g.siteBoards.get("Market"));
        bmc.market =  (Market) g.siteBoards.get("Market");

        u1.player.marketCards.add(bmc);

        Move m = new Move("PlayerCardStack", "Market", bmc.id);
        m.ShipID = 0;
        m.setUser(u1);

        // should have 2
        Assert.assertEquals((Integer)2, u1.player.sled.sledLoad);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));

        // step 1
        Assert.assertNotNull(u1.player.decision.actions);
        m = u1.player.decision.actions.get(0).moves.get(0);
        m.setUser(u1);
        Assert.assertNotNull(m);

        Assert.assertEquals("Sled",m.from);
        Assert.assertEquals("DepartingHarbour",m.to);

        Assert.assertTrue(g.turnManager.executePlayerMove(m));


        // now should only have 1
        Assert.assertEquals((Integer)1, u1.player.sled.sledLoad);

        // step 2
        Assert.assertNotNull(u1.player.decision.actions);
        m = u1.player.decision.actions.get(0).moves.get(0);
        m.setUser(u1);
        Assert.assertNotNull(m);

        Assert.assertEquals("DepartingHarbour",m.from);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));



        Assert.assertTrue(harbour.ships.get(1).setStone(0, PlayerFieldColour.BLACK));

        u1.player.marketCards.add(bmc);
        m = new Move("PlayerCardStack", "Market", bmc.id);
        m.ShipID = 1;
        m.setUser(u1);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));

        m = getFirstMove(u1);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));

        // now it's gone
        Assert.assertEquals((Integer)0, u1.player.sled.sledLoad);

        // can't ship it anymore
        Assert.assertFalse( ((PlayerCardStack) g.siteBoards.get("PlayerCardStack")).validateMoveImplementation(m));
        Assert.assertFalse(g.turnManager.executePlayerMove(m));



    }

    @Test
    public void hammerTestWithTM() throws Exception {

        BlueMarketCard bmc = getCard(ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.BlueMarketCard.BlueType.Hammer, (Market) g.siteBoards.get("Market"));
        bmc.market =  (Market) g.siteBoards.get("Market");

        u1.player.marketCards.add(bmc);

        Move m = new Move("PlayerCardStack", "Market", bmc.id);
        m.ShipID = 0;
        m.setUser(u1);

        Assert.assertTrue(g.turnManager.executePlayerMove(m));

        // step 1
        Assert.assertNotNull(u1.player.decision.actions);
        m = u1.player.decision.actions.get(0).moves.get(0);
        m.setUser(u1);
        Assert.assertNotNull(m);
        Assert.assertEquals("Quarry",m.from);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));


        // step 2
        Assert.assertNotNull(u1.player.decision.actions);
        m = u1.player.decision.actions.get(0).moves.get(0);
        m.setUser(u1);
        Assert.assertNotNull(m);

        Assert.assertEquals("Sled",m.from);
        Assert.assertEquals("DepartingHarbour",m.to);
        Assert.assertTrue(g.turnManager.executePlayerMove(m));


    }


    @Test
    public void turnManagerBlueCardTest() throws MoveFactory.MoveFactoryException {
        HashMap<String,SiteBoard> sb = new HashMap<>();
        Game game = new Game();

    }


    @Test
    public void points() throws Exception {

        MarketCard mc = new MarketCard(MarketCard.Type.Red,1);
        Assert.assertEquals(1,mc.gimmepoints);

        BlueMarketCard bmc = new BlueMarketCard(BlueMarketCard.BlueType.Sail, 1, sa, ssa);

        Move m = new Move();
        m.setUser(u1);
        bmc.myType = null;
        Assert.assertFalse(bmc.playCard(m));
    }

    private BlueMarketCard getCard(BlueMarketCard.BlueType type, Market market){

        for(MarketCard mc : market.allCards)
            if(mc.type == MarketCard.Type.Blue ) {
                BlueMarketCard bmc = (BlueMarketCard) mc;
                if(bmc.myType == type)
                    return bmc;
            }

            return null;
    }

    private Move getFirstMove(User u) {
        Move m = u.player.decision.actions.get(0).moves.get(0);
        m.setUser(u);
        return m;
    }

}
