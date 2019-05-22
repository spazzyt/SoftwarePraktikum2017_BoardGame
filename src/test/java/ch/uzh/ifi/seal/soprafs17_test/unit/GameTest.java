package ch.uzh.ifi.seal.soprafs17.unit;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.gamelogic.DockableSite;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class GameTest {

    @Autowired
    UserService userService;

    @Test
    public void testBrudi() throws Exception {
        Game g = new Game();

        User a = userService.createUser("brudi-GT");
        User b = userService.createUser("brudi2-GT");

        g.addPlayer(a);
        g.addPlayer(b);

        assertNotNull(g.getPlayers());
        assertTrue(g.removePlayer(b));

        User c = userService.login("brudi-GT");
        assertTrue(g.removePlayer(a));
    }


    @Test
    public void testSetup() throws Exception {

        Game g = new Game();
        g.setNumPlayers(2);
        g.setUpGame();

        assertEquals(g.siteBoards.size(),6);
        assertTrue(g.siteBoards.containsKey("Obelisk"));
        assertTrue(g.siteBoards.containsKey("Pyramid"));
        assertTrue(g.siteBoards.containsKey("BurialChamber"));
        assertTrue(g.siteBoards.containsKey("Market"));
        assertTrue(g.siteBoards.containsKey("Temple"));
        assertTrue(g.siteBoards.containsKey("DepartingHarbour"));
        assertEquals(g.siteBoards.get("Obelisk").siteId, "Obelisk");

        assertTrue(g.turnManager.ds.contains( (DockableSite) g.siteBoards.get("Obelisk")));
        assertTrue(g.turnManager.ds.contains( (DockableSite) g.siteBoards.get("Pyramid")));
        assertTrue(g.turnManager.ds.contains( (DockableSite) g.siteBoards.get("BurialChamber")));
        //assertTrue(g.turnManager.ds.contains( (DockableSite) g.siteBoards.get("Market")));
        assertTrue(g.turnManager.ds.contains( (DockableSite) g.siteBoards.get("Temple")));
    }

    @Test
    public void someFails() {

        Game g = new Game();
        Assert.assertNull(g.getUser("xxx"));
        Assert.assertNull(g.getPlayer("xxx"));

    }
}
