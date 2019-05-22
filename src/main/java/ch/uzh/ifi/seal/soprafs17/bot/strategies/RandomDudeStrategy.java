package ch.uzh.ifi.seal.soprafs17.bot.strategies;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.Random;

/**
 * Created by nairboon on 07.05.17.
 */
public class RandomDudeStrategy implements BotStrategy{

    protected Random random = new Random();

    @Override
    public Move pickMove(Decision des) {
        int action = this.random.nextInt(des.actions.size());
        int move = this.random.nextInt(des.actions.get(action).moves.size());
        return des.actions.get(action).moves.get(move);
    }
}
