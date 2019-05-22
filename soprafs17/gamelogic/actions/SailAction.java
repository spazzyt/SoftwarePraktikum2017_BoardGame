package ch.uzh.ifi.seal.soprafs17.gamelogic.actions;


import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.DockableSite;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.MoveFactory;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.ShipMoveFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SailAction extends Action {

    private final Logger log = LoggerFactory.getLogger(SailAction.class);

    public ShipMoveFactory factory;

    public SailAction(DepartingHarbour dh,
                      List<DockableSite> ds)  {

        super(Type.Sail);
        try {
            factory = new ShipMoveFactory(dh,ds);
        } catch (MoveFactory.MoveFactoryException e) {
            log.error("Exception", e);
        }
        super.factory = factory;

    }

}
