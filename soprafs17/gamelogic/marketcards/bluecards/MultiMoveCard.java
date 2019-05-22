package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.bluecards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

/**
 * Created by nairboon on 09.05.17.
 */
public abstract class MultiMoveCard {

    public int comboMove = 0;

    public Player player;

    protected Move lastMove;

    // checks if the combo move is over
    public boolean donePlaying() {
        return comboMove == 0;
    }

    public Action step(Move m) {
        lastMove = m;
        Action a = stepImplementation();
        comboMove--;
        return a;
    }

    protected abstract Action stepImplementation();
}
