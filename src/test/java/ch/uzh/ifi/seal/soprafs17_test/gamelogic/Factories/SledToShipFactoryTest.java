package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Spasen on 18.04.17.
 */
public class SledToShipFactoryTest {

    public Integer sizes[] = {1,2,3,4}; //sum = 10
    public DepartingHarbour dh = new DepartingHarbour();


    //List<DestinationSite> ds = new ArrayList<>();
    public User user = new User();
    public Player player = new Player(PlayerFieldColour.BLACK,1);
    public List<Move> results = new ArrayList<>();
    public SledToShipFactory sledToShipFactory;

    public SledToShipFactoryTest() throws MoveFactory.MoveFactoryException {
        sledToShipFactory = new SledToShipFactory(dh);
    }

    @Before
    public void setup() throws Exception {
        user.player = player;
    }

    @Test
    public void failingFactory() {
        try {
            SledToShipFactory factory = new SledToShipFactory(null);
            fail("Method didn't throw");
        } catch (MoveFactory.MoveFactoryException e) {
        }
    }

    @Test
    public void createMoves(){
        dh.setSizeOfShips(sizes);
        results = sledToShipFactory.createMoves(player);


        assertNotNull(results);
        //assertEquals((Integer) 1, sledToShipFactory.pos);
        assertEquals(10,results.size());
        assertEquals("Sled", results.get(0).from);
        assertEquals("DepartingHarbour", results.get(0).to);


    }

    @Test
    public void testCreatedMove(){
        dh.setSizeOfShips(sizes);
        results = sledToShipFactory.createMoves(player);

        assertNotNull(results);
        Move m = results.get(0);
        m.setUser(this.user);

        Assert.assertFalse(dh.ships.get(0).movable());

        //actual placed stones on the sled
        Assert.assertEquals((Integer)1, player.sled.sledLoad);


        Assert.assertTrue(player.sled.validateMoveImplementation(results.get(0)));
        Assert.assertTrue(dh.validateMoveImplementation(m));
/*
        Assert.assertTrue(player.sled.executeMoveImplementation(m));
        Assert.assertTrue(dh.executeMoveImplementation(m));

        Assert.assertTrue(dh.ships.get(0).movable());
        */
    }

    @Test
    public void createMovesSailCard(){
        dh.setSizeOfShips(sizes);

        Assert.assertFalse(dh.ships.get(0).movable());

        results = sledToShipFactory.createMovesSailCard(player);

        assertNotNull(results);
        assertTrue(results.size() > 0);

        Move toFirstShip = results.get(0);
        assertEquals( (Integer) 0, toFirstShip.ShipID);
        assertEquals( (Integer) 0, toFirstShip.pos);

        Move toSecondShipFirst = results.get(1);
        assertEquals( (Integer) 1, toSecondShipFirst.ShipID);
        assertEquals( (Integer) 0, toSecondShipFirst.pos);

        Move toSecondShipSecond = results.get(2);
        assertEquals( (Integer) 1, toSecondShipSecond.ShipID);
        assertEquals( (Integer) 1, toSecondShipSecond.pos);
    }

}
