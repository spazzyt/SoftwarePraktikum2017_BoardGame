package ch.uzh.ifi.seal.soprafs17.gamelogic.actions;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Factories.QuarryToSledFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Spasen on 13.04.17.
 */
public class QuarryToSledAction extends Action{

    @JsonIgnore
    public QuarryToSledFactory factory;

    public QuarryToSledAction() {

        super(Type.Quarry);
        factory = new QuarryToSledFactory();
        super.factory = factory;

    }
}
