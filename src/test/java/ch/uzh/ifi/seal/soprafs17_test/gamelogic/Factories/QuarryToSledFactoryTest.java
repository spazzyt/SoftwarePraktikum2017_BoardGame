package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Quarry;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Sled;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Spasen on 13.04.17.
 */


public class QuarryToSledFactoryTest {



    @Test
    public void testCreateMoves() throws MoveFactory.MoveFactoryException {
        int sledSize = 2;
        Player p = new Player(PlayerFieldColour.BLACK, sledSize);
        List<Move> results = null;
        QuarryToSledFactory factory = new QuarryToSledFactory();


        //quarry to sled
        Move quarryToSled = new Move();
        assertEquals((Integer) 2, p.sled.sledLoad);
        assertEquals(3, p.sled.getSpaceCount());

        quarryToSled.from = "Quarry";
        quarryToSled.to = "Sled";
        quarryToSled.pos = p.sled.getSpaceCount(); //sets max possible stones on quarry

        assertNotNull(quarryToSled);
        assertEquals((Integer) 3, quarryToSled.pos);
        assertEquals("Quarry", quarryToSled.from);
        assertEquals("Sled", quarryToSled.to);

        Assert.assertTrue(p.sled.validateMoveImplementation(quarryToSled));
        Assert.assertTrue(p.sled.executeMoveImplementation(quarryToSled));
        assertEquals((Integer) 5, p.sled.sledLoad);


        assertEquals((Integer) 3, quarryToSled.pos);

        // clear sled
        p.sled.sledLoad = 0;

        results = factory.createMoves(p);
        assertNotNull(results);
        assertTrue(results.size()>0);

        assertEquals(1, results.size());
        assertEquals("Quarry", results.get(0).from);
        assertEquals("Sled", results.get(0).to);
        assertEquals((Integer) 3, quarryToSled.pos);

        // TODO: ???
      //  assertEquals(0, p.sled.getSpaceCount()); //min a:3, b:5-sledload

        //assertEquals(p.sled.playerFieldColour, p.sled.getCount(PlayerFieldColour.BLACK));




    }

    @Test
    public void testCreateMovesFactory() throws MoveFactory.MoveFactoryException {
        int sledSize = 2;
        Player p = new Player(PlayerFieldColour.BLACK, sledSize);
        List<Move> results = null;
        QuarryToSledFactory factory = new QuarryToSledFactory();

        Assert.assertNotNull(factory);
        Assert.assertNull(results);

        results = factory.createMoves(p);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());

        Assert.assertEquals((Integer)3, results.get(0).pos);
        Assert.assertEquals("Quarry", results.get(0).from);
        Assert.assertEquals("Sled", results.get(0).to);

        //Quarry to sled execute
        Assert.assertTrue(p.sled.validateMoveImplementation(results.get(0)));
        Assert.assertTrue(p.sled.executeMoveImplementation(results.get(0)));
        Assert.assertEquals((Integer)5, p.sled.sledLoad);
    }


    @Test
    public void testForDave(){
        User user1 = new User();
        Player p1 = new Player(PlayerFieldColour.BLACK,2);
        Move m1 = new Move();
        m1.from = "Quarry";
        m1.to = "Sled";
        m1.pos = 3;

        Assert.assertTrue(p1.sled.validateMoveImplementation(m1));
        Assert.assertTrue(p1.sled.validateMove(m1));
        Assert.assertEquals(3, p1.sled.getSpaceCount());

        p1.sled.sledLoad = 0;
        Assert.assertEquals(3, p1.sled.getSpaceCount());
    }

    @Test
    public void testQuarry(){

        Player player = new Player(PlayerFieldColour.BLACK,2);
        Sled sled = new Sled();
        Quarry quarry = new Quarry();

        Move falseMove = new Move();
        falseMove.from =  "hello";
        falseMove.to =  "hello";
        falseMove.pos = 100;

        Assert.assertFalse(player.quarry.validateMoveImplementation(falseMove));
        Assert.assertFalse(player.quarry.executeMoveImplementation(falseMove));
        Assert.assertEquals(0, player.quarry.assessPoints(PlayerFieldColour.BLACK));

        Assert.assertFalse(player.sled.executeMoveImplementation(falseMove));
        Assert.assertEquals(0, player.sled.assessPoints(PlayerFieldColour.BLACK));



    }
}