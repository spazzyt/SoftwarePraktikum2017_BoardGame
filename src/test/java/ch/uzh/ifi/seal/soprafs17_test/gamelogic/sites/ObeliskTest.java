package ch.uzh.ifi.seal.soprafs17.gamelogic.sites;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Obelisk;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.ShipToken;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nairboon on 27.03.17.
 */
/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
*/
public class ObeliskTest {

    @Test
    public void testRankPlayer() throws Exception {

        //TODO find out what "Failed to load ApplicationContext" is
        Obelisk obelisk = new Obelisk(2);

        obelisk.fields[0].playerColour = PlayerFieldColour.BLACK;
        obelisk.fields[0].amount = 3;

        obelisk.fields[1].playerColour = PlayerFieldColour.GREY;
        obelisk.fields[1].amount = 8;

        assertEquals(obelisk.rankPlayer(PlayerFieldColour.GREY),0);
        assertEquals(obelisk.rankPlayer(PlayerFieldColour.BLACK),1);
    }


    @Test
    public void testSetStone(){
        //EMPTY, BLACK, WHITE, BROWN, GREY
        Obelisk obelisk = new Obelisk(4);
        obelisk.setStone(PlayerFieldColour.BLACK);
        assertEquals(obelisk.fields[0].amount, 1);
        assertEquals(1, obelisk.stoneCounter);

        obelisk.setStone(PlayerFieldColour.EMPTY);
        assertEquals(obelisk.fields[1].amount, 0);

        obelisk.setStone(PlayerFieldColour.GREY);
        assertEquals(obelisk.fields[3].amount, 1);

    }


    @Test
    public void twoPlayersShips() throws Exception {

        Integer sizes[] = {1,2,3,4};
        Obelisk obelisk = new Obelisk(2);
        Obelisk obelisk2 = new Obelisk(2);
        DepartingHarbour dh = new DepartingHarbour();
        dh.setSizeOfShips(sizes);
        assertFalse(dh.ships.get(1).movable());


        //first ship
        ShipToken ship1 = dh.ships.get(0);
        Assert.assertEquals(1,ship1.numFields);
        Assert.assertTrue(ship1.setStone(0,PlayerFieldColour.BLACK));
        Assert.assertTrue(ship1.movable());

        //second ship
        ShipToken ship2 = dh.ships.get(1);
        Assert.assertEquals(2,ship2.numFields);

        Assert.assertTrue(ship2.setStone(0,PlayerFieldColour.BLACK));
        Assert.assertTrue(ship1.movable());
        Assert.assertTrue(ship2.movable());

        Assert.assertTrue(ship2.setStone(1,PlayerFieldColour.WHITE));
        Assert.assertTrue(ship1.movable());
        Assert.assertTrue(ship2.movable());

        assertFalse(ship2.setStone(0,PlayerFieldColour.BLACK));
        assertFalse(ship2.setStone(1,PlayerFieldColour.BLACK));

        Move m = new Move();
        m.pos = 0;
        m.from="DepartingHarbour";
        m.to ="Obelisk";
        m.toSite = obelisk;
        //m.ship = ship1;

        assertTrue(dh.validateMoveImplementation(m));
        assertTrue(obelisk.validateMoveImplementation(m));
        assertTrue(dh.executeMoveImplementation(m));
        assertTrue(obelisk.executeMoveImplementation(m));

        //assertTrue(dh.executeMoveImplementation(m));
        m.toSite = obelisk2;

        m.pos = 1;
        assertTrue(dh.validateMoveImplementation(m));
        assertTrue(obelisk2.validateMoveImplementation(m));
        assertTrue(dh.executeMoveImplementation(m));
        assertTrue(obelisk2.executeMoveImplementation(m));

        assertNull(dh.ships.get(0));
        assertNull(dh.ships.get(1));

        assertFalse(dh.validateMoveImplementation(m));
        assertFalse(obelisk2.validateMoveImplementation(m));
        assertFalse(dh.executeMoveImplementation(m));
        //validateMoveImplementation checks wether it is valid or not (is called everytime before execute)
        assertTrue(obelisk2.executeMoveImplementation(m));

        assertEquals(obelisk.assessPoints(PlayerFieldColour.BLACK), 10);

    }


    @Test
    public void threePlayersShips() throws Exception {

        Obelisk obelisk = new Obelisk(3);


        ShipToken ship = new ShipToken(1,0);
        ship.fields[0] = PlayerFieldColour.BLACK.ordinal();

        Move m = new Move();
        m.to ="Obelisk";
        m.toSite = obelisk;
        m.ship = ship;

        obelisk.ship = ship;

        assertTrue(obelisk.validateMove(m));
        assertTrue(obelisk.executeMove(m));

        assertEquals(obelisk.assessPoints(PlayerFieldColour.BLACK), 12);

        // for coverage
        assertEquals(-1, obelisk.rankPlayer(PlayerFieldColour.EMPTY));
    }



}
