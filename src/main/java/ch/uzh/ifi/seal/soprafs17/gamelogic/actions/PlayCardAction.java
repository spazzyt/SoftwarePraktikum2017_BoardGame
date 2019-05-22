package ch.uzh.ifi.seal.soprafs17.gamelogic.actions;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.PlayCardFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by nairboon on 07.05.17.
 */
public class PlayCardAction extends Action {

    @JsonIgnore
    public PlayCardFactory factory;

    public PlayCardAction() {

        super(Action.Type.PlayCard);
        factory = new PlayCardFactory();
        super.factory = factory;
    }
}