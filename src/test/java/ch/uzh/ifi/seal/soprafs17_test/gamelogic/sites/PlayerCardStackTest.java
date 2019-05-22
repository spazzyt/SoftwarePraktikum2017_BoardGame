package ch.uzh.ifi.seal.soprafs17.gamelogic.sites;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.gamelogic.PlayerCardStack;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nairboon on 10.05.17.
 */
public class PlayerCardStackTest {

    @Test
    public void validates() {

        Player p = new Player();

        PlayerCardStack pcs = new PlayerCardStack(p);

        Move m = new Move("PlayerCardStack", "Market", 99);
        Assert.assertFalse(pcs.executeMoveImplementation(m));

        m = new Move("Nix","yoo",2);
        Assert.assertFalse(pcs.validateMoveImplementation(m));


    }
}
