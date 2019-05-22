package ch.uzh.ifi.seal.soprafs17.unit;


import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.ShipToken;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.gamelogic.DockableSite;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Obelisk;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SailAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SledToShipAction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
public class StuffTest {


    @Test
    public void random() {
        DockableSite ds = new DockableSite() {
            @Override
            public boolean executeMoveImplementation(Move m) {
                return false;
            }

            @Override
            public boolean validateMoveImplementation(Move m) {
                return false;
            }

            @Override
            public int assessPoints(PlayerFieldColour c) {
                return 0;
            }

            @Override
            public boolean isCorrectZone(Move m) {
                return false;
            }
        };

        // for tests
        ds.setStone(PlayerFieldColour.BLACK);
    }


    @Test
    public void shipUpdateStones() {

        ShipToken ship = new ShipToken(4,1);

        ArrayList<Integer> no = new ArrayList<>();
        no.add(1);

        Assert.assertFalse(ship.updateStones(no));
        no.add(2);
        no.add(3);
        no.add(2);
        Assert.assertTrue(ship.updateStones(no));

        no.set(2,99);
        Assert.assertFalse(ship.updateStones(no));
    }


    @Test
    public void someExceptions() {
        SledToShipAction ssa = new SledToShipAction(null);
        Assert.assertNull(ssa.factory);

        SailAction sa = new SailAction(null,null);
        Assert.assertNull(sa.factory);
    }

    @Test
    public void redMove() {
        User u1 = new User("bob");
        User u2 = new User("pepe");

        Game g = new Game();
        g.setNumPlayers(2);
        g.addPlayer(u1);
        g.addPlayer(u2);

        g.setUpGame();

        g.start();

        Move m = new Move("Nope","Nopino",0);
       Assert.assertFalse(g.turnManager.executeRedMove(m));

        m = new Move("Market","Sled",0);
        Assert.assertFalse(g.turnManager.executeRedMove(m));
    }
}
