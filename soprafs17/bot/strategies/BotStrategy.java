package ch.uzh.ifi.seal.soprafs17.bot.strategies;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

/**
 * Created by nairboon on 07.05.17.
 */
public interface BotStrategy {
    Move pickMove(Decision des);
}
