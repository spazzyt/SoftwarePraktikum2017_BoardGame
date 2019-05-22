package ch.uzh.ifi.seal.soprafs17.gamelogic.RoundCards;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.gamelogic.RoundCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Spasen on 11.04.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class RoundCardTest {


    @Test
    public void roundCard(){
        RoundCard roundCard4 = new RoundCard(4);
        RoundCard roundCard3 = new RoundCard(3);
        RoundCard roundCard2 = new RoundCard(2);

        assertEquals(roundCard4.activeArray.size(),6);
        assertEquals(roundCard3.activeArray.size(),6);
        assertEquals(roundCard2.activeArray.size(),6);
    }


}
