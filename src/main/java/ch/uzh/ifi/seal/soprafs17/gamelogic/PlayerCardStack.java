package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.BlueMarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;

/**
 * Created by nairboon on 22.04.17.
 */

@Embeddable
public class PlayerCardStack extends OriginAndDepartingSite implements Command{

    private final Logger log = LoggerFactory.getLogger(PlayerCardStack.class);


    @JsonIgnore
    public Player player;


    public PlayerCardStack(Player player){
        this.player = player;

    }

    @Override
    public boolean validateMoveImplementation(Move m) {

        if(m.from.equals("Market") && m.to.equals("PlayerCardStack")){
            return true;
        }

        if(m.from.equals("PlayerCardStack") && m.to.equals("Market")){
            //besitzt man die karte wirklich
            MarketCard card = null;
            for(MarketCard mc : player.marketCards){
                if(mc.id.equals(m.pos)) {
                    card = mc;
                }
            }

            if(card == null)
                return false;

            if(card.type == MarketCard.Type.Blue && !((BlueMarketCard)card).isPlayable(player)) {
                log.warn("Card is not playable" + card.id);
                return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean executeMoveImplementation(Move m){
        if(m.from.equals("PlayerCardStack") && m.to.equals("Market")) {
            MarketCard card = null;
            for (MarketCard mc : player.marketCards) {
                if (mc.id.equals(m.pos)) {
                    card = mc;
                    player.marketCards.remove(mc);
                    break;
                }
            }

            if(card == null)
                return false;

            /* DON'T double play the card: over at Market.java:276
             * only check that the card is here
            */



        }
        return true;
    }



    //needs to be added to siteboards in turn

    @Override
    public int assessPoints(PlayerFieldColour c) {
        Integer [] pointsStatue = {0,1,3,6,10,15,17,19,21,23,25};
        int counterBlue = 0;
        int counterStatue = 0;
        int greenPoints = 0;
        for (MarketCard mc : player.marketCards) {
            if(mc.type == MarketCard.Type.Statue)
                counterStatue++;

            if(mc.type == MarketCard.Type.Green) {
                greenPoints += mc.gimmepoints;

            }

        }
        return counterBlue + pointsStatue[counterStatue] + greenPoints;
    }
}
