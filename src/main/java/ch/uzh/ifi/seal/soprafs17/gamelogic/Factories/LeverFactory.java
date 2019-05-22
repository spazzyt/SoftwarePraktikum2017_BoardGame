package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nairboon on 09.05.17.
 */
public class LeverFactory implements MoveFactory{

    private final Logger log = LoggerFactory.getLogger(LeverFactory.class);

    public DepartingHarbour harbour;

    public LeverFactory(DepartingHarbour dh) {

        harbour = dh;

    }

    @Override
    public List<Move> createMoves(Player p) {

        List<Move> results = new ArrayList<>();

        for(int i : this.harbour.ships.keySet()) {

            // for a moveable ship
            if(harbour.ships.get(i).movable()) {

                // generate stone permutations...

                // or just fake it xD
                ArrayList<Integer> shipOrder = harbour.ships.get(i).asList();
                Collections.shuffle(shipOrder);

                Move m = new Move("DepartingHarbour", "DepartingHarbour",0);
                m.ShipID = i;
                m.pos = i;
                m.LeverStones = shipOrder;
                results.add(m);
            }

        }

        return results;
    }
}
