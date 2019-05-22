package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.entity.Move;

/**
 * Created by nairboon on 27.03.17.
 */
public interface Command {

    boolean validateMove(Move m);
    boolean executeMove(Move m);
    boolean validateMoveImplementation(Move m);
    boolean executeMoveImplementation(Move m);

}
