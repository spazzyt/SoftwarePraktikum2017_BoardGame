package ch.uzh.ifi.seal.soprafs17.gamelogic.actions;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DepartingHarbour;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.LeverFactory;

/**
 * Created by nairboon on 09.05.17.
 */
public class LeverAction   extends  Action{

    public LeverFactory factory;

    public LeverAction(DepartingHarbour dh)  {

        super(Type.Pickup);
        factory = new LeverFactory(dh);
        super.factory = factory;

    }

}