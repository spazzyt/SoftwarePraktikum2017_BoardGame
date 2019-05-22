package ch.uzh.ifi.seal.soprafs17.bot.strategies;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

/**
 * Created by nairboon on 07.05.17.
 *
 * Picks the first move of the first action
 *
 */
public class FirstMoveStrategy implements BotStrategy{

    @Override
    public Move pickMove(Decision des) {
        return des.actions.get(0).moves.get(0);
    }
}
