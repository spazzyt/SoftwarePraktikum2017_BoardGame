package ch.uzh.ifi.seal.soprafs17.gamelogic.actions;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.MoveFactory;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.SledToShipFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Spasen on 11.04.17.
 */
public class SledToShipAction extends Action {

    private final Logger log = LoggerFactory.getLogger(SledToShipAction.class);

    public SledToShipFactory factory;

    public SledToShipAction(DepartingHarbour dh) {
        super(Type.Sled);
        try {
            factory = new SledToShipFactory(dh);
        } catch (MoveFactory.MoveFactoryException e) {
            log.error("Exception", e);
        }
        super.factory = factory;

    }

}
