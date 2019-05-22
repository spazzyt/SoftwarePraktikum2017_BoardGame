package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.DockableSite;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Obelisk;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Spasen on 05.04.17.
 */
public class ShipMoveFactoryTest {

    public Integer size[] = {1,2,3,4};
    public int sledSize = 2;

    @Test
    public void failingFactory() {
        try {
            ShipMoveFactory factory = new ShipMoveFactory(null, null);
            fail("Method didn't throw");
        } catch (MoveFactory.MoveFactoryException e) {
        }
    }

    @Test
    public void testCreateMoves() throws Exception{

        //creating a new player, harbour, obelisk for two players
        Player player = new Player(PlayerFieldColour.BLACK, sledSize);
        DepartingHarbour harbour = new DepartingHarbour();
        harbour.setSizeOfShips(size);
        Obelisk obelisk = new Obelisk(2);

        //create a list with possible destinationSites, starting with obelisk
        List<DockableSite> finalDestination = new ArrayList<>();
        finalDestination.add(obelisk);

        /* harbour.ships.get(x).setStone returns true if position is valid
        [x]
        [x][]
        and if the stone could be set */
        Assert.assertTrue(harbour.ships.get(0).setStone(0, PlayerFieldColour.BLACK));
        Assert.assertTrue(harbour.ships.get(1).setStone(0, PlayerFieldColour.BLACK));

        //are false because there is already a stone set
        Assert.assertFalse(harbour.ships.get(0).setStone(0, PlayerFieldColour.GREY));
        Assert.assertFalse(harbour.ships.get(1).setStone(0, PlayerFieldColour.BROWN));
        Assert.assertFalse(harbour.ships.get(1).setStone(0, PlayerFieldColour.WHITE));

        /* harbour.ships.get(0) has a size() of 1 and number of set stones 1
        therefore the first and the second ship are movable now */
        Assert.assertTrue(harbour.ships.get(0).movable());
        Assert.assertTrue(harbour.ships.get(1).movable());

        //note that the third and fourth ship is not movable, since the requirements are not met
        Assert.assertFalse(harbour.ships.get(2).movable());
        Assert.assertFalse(harbour.ships.get(3).movable());

        ShipMoveFactory shipMoveFactory = new ShipMoveFactory(harbour, finalDestination);

        List<Move> moves = shipMoveFactory.createMoves(player);


        //valid move--> (0),(1),(2),(3)
        Move shipToSite_isValid = new Move();
        shipToSite_isValid.from = "DepartingHarbour";
        shipToSite_isValid.to = "Obelisk";
        shipToSite_isValid.pos = 0;

        Assert.assertTrue(obelisk.validateMoveImplementation(shipToSite_isValid));
        Assert.assertTrue(harbour.validateMoveImplementation(shipToSite_isValid));



        //on two ships (1.ship with size=1 and on 2.ship with size=2)
        assertEquals(2, moves.size());


        Move mp1 = moves.get(0);
        assertEquals("DepartingHarbour", mp1.from );
        assertEquals("Obelisk", mp1.to );
        assertEquals( (Integer)0, mp1.pos );


        Move mp2 = moves.get(1);
        assertEquals("DepartingHarbour", mp2.from );
        assertEquals("Obelisk", mp2.to );
        assertEquals( (Integer)1, mp2.pos );





    }




}
