package ch.uzh.ifi.seal.soprafs17.bot.strategies;

import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Decision;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.Arrays;

/**
 * Created by nairboon on 07.05.17.
 */
public class CardLoverStrategy extends RandomDudeStrategy {

    @Override
    public Move pickMove(Decision des) {

        Action playCard = null;
        // search for card moves
        for(Action a : des.actions) {
            if(a.type == Action.Type.PlayCard) {
                playCard = a;
                break;
            }
        }

        // delegate to random strategy
        if(playCard == null) {
            return super.pickMove(des);
        }

       // Integer chisel[] = {24,25,26};
        Integer sail[] = {31,32,33};


        for(Move m : playCard.moves){

            // find the chiselcard
            if(Arrays.asList(sail).contains(m.pos)) {
                return m;
            }
        }
        // which card?
        int card = this.random.nextInt(playCard.moves.size());
        return playCard.moves.get(card);
    }
}
