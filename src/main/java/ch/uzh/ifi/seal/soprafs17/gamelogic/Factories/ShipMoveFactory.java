package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.DockableSite;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ShipMoveFactory implements MoveFactory {



    private final Logger log = LoggerFactory.getLogger(ShipMoveFactory.class);

    public DepartingHarbour harbour;
    public List<DockableSite> destinations;


    public ShipMoveFactory(DepartingHarbour dh,
                           List<DockableSite> ds) throws MoveFactoryException {
        if(dh == null || ds.size() == 0) {
            log.error("Departing Harbour or Dockable sites null");
            throw new MoveFactoryException();
        }

        harbour = dh;
        destinations = ds;
    }

    @Override
    public List<Move> createMoves(Player p) { //p wird nicht eingesetzt

        // list results = departable ships
        List<Move> results = new ArrayList<>();

        for(int i : this.harbour.ships.keySet()) {
            Move m = new Move();
            m.from= "DepartingHarbour";
            m.pos = i;

            if( harbour.validateMove(m)) {
                for(DockableSite d : destinations) {
                    Move md = new Move(m);
                    md.to = d.siteId;
                    if (d.validateMove(md)) {
                        Move newMove = new Move();
                        newMove.to = md.to;
                        newMove.from = m.from;
                        newMove.pos = m.pos;
                        results.add(newMove);
                    }
                }
            }
        }

        // list b = dockable target

        // foreach destinationsite

        // results = intersect(a,b)


        return results;
    }
}
