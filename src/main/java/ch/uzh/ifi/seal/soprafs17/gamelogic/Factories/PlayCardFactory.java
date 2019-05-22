package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.BlueMarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nairboon on 07.05.17.
 */
public class PlayCardFactory implements MoveFactory {

    @Override
    public List<Move> createMoves(Player p) {

        List<Move> results = new ArrayList<>();

        // does the user has some playable cards?

        for(MarketCard mc : p.marketCards) {

            Move m = new Move("PlayerCardStack","Market", mc.id);

           // we play some blue ones
            if(mc.type == MarketCard.Type.Blue) {

                BlueMarketCard bmc = (BlueMarketCard) mc;
                if( ( bmc.myType == BlueMarketCard.BlueType.Chisel
                       || bmc.myType == BlueMarketCard.BlueType.Sail
                       ||  bmc.myType == BlueMarketCard.BlueType.Hammer
                       ||  bmc.myType == BlueMarketCard.BlueType.Lever
                ) &&  bmc.isPlayable(p))
                    results.add(m);
            }
        }

        return results;
    }
}
