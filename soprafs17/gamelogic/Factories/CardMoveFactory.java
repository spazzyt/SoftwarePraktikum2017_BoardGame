package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Market;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spasen on 10.04.17.
 */
public class CardMoveFactory implements MoveFactory{

    public Market market;

    public CardMoveFactory(Market m) {
        this.market = m;
    }
    @Override
    public List<Move> createMoves(Player p) {


        List<Move> results = new ArrayList<>();
        if(market.ship == null){
            return results;
        }

        for(MarketCard mc : this.market.pepeCards) {
            Move m = new Move();
            m.from = "Market";
            m.to = "PlayerCardStack";
            m.pos = mc.id; //einfache durchnummerierung durch die Statuen-Karten, ist es das?
            //todo erkären was m.pos für cardmovefactory bedeuted

            results.add(m);
        }
        return results;
    }
}
