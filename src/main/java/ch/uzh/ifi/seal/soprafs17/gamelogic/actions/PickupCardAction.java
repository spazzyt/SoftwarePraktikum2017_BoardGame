package ch.uzh.ifi.seal.soprafs17.gamelogic.actions;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.CardMoveFactory;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Market;

/**
 * Created by nairboon on 21.04.17.
 */
public class PickupCardAction  extends  Action{

    public CardMoveFactory factory;

    public PickupCardAction(Market m) {

        super(Type.Pickup);
        factory = new CardMoveFactory(m);
        super.factory = factory;

    }

}
