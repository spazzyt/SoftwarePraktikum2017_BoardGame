package ch.uzh.ifi.seal.soprafs17.gamelogic.sites;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Spasen on 27.03.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class HarbourTest {


    public Integer size[] = {1,2,3,4};

    @Test
    public void setSizeOfShips(){

        DepartingHarbour var = new DepartingHarbour();
        var.setSizeOfShips(size);
        Assert.assertEquals(var.ships.size(), 4);//


    }

    @Test
    public void shipInHarbour(){
        DepartingHarbour harbour = new DepartingHarbour();

    }

    //Test if empty ship can be moved
    @Test
    public void moveEmptyShipTest(){
        Move m = new Move();
        m.from = "";
        m.to = "";
        DepartingHarbour harbour = new DepartingHarbour();
        harbour.setSizeOfShips(size);
        Assert.assertFalse(harbour.validateMove(m));

        Assert.assertFalse(harbour.ships.get(0).movable());
        Assert.assertFalse(harbour.ships.get(1).movable());
        Assert.assertFalse(harbour.ships.get(2).movable());
        Assert.assertFalse(harbour.ships.get(3).movable());

        Assert.assertTrue(harbour.ships.get(0).soonMovable());
        Assert.assertTrue(harbour.ships.get(1).soonMovable());
        Assert.assertFalse(harbour.ships.get(2).soonMovable());
        Assert.assertFalse(harbour.ships.get(3).soonMovable());



    }

    @Test
    public void fullShipTest(){
        DepartingHarbour harbour = new DepartingHarbour();
        harbour.setSizeOfShips(size);
        Assert.assertTrue(harbour.ships.get(0).setStone(0, PlayerFieldColour.BLACK));
        Assert.assertTrue(harbour.ships.get(0).movable());
        Move m = new Move();

        m.pos = 0;

        //Test has to fail because von Gore is not a site
        m.from = "von Gore";
        Assert.assertFalse(harbour.validateMove(m));
        //Test has to succeed because DepartingHarbour is the correct site
        //move is valid
        m.from = "DepartingHarbour";
        Assert.assertTrue(harbour.validateMove(m));

    }

    @Test
    public void invalidMoves() {
        DepartingHarbour harbour = new DepartingHarbour();
        harbour.setSizeOfShips(size);

        Move m = new Move("Irgendwas", "irgendwohin",0);
        Assert.assertFalse(harbour.validateMoveImplementation(m));
        Assert.assertFalse(harbour.executeMoveImplementation(m));
        Assert.assertEquals(0,harbour.assessPoints(PlayerFieldColour.EMPTY));

        m = new Move("DepartingHarbour", "DepartingHarbour",42);
        Assert.assertFalse(harbour.validateMoveImplementation(m));
        Assert.assertFalse(harbour.executeMoveImplementation(m));
    }

    @Test
    public void testSoonMovable(){
        DepartingHarbour harbour = new DepartingHarbour();
        harbour.setSizeOfShips(size);
        Assert.assertEquals((Integer)2, harbour.gibSoonMovable());


    }
}
