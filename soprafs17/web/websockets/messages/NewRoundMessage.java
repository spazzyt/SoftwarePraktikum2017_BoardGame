package ch.uzh.ifi.seal.soprafs17.web.websockets.messages;

import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.entity.ShipToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Spasen on 18.04.17.
 */
public class NewRoundMessage {

    public int roundNumber = 0;
    private List<MarketCard> marketCards;
    public List<ShipToken> ships = new ArrayList<>();
    public Map<String,Integer> berlinerScore = new HashMap<>();

    public Map<String,Integer> quarries = new HashMap<>();
    public Map<String,Integer> sleds = new HashMap<>();

    public NewRoundMessage(int roundNumber){
        this.roundNumber = roundNumber;
    }


    public List<MarketCard> getMarketCards() {
        return marketCards;
    }

    public void setMarketCards(List<MarketCard> marketCards) {
        this.marketCards = marketCards;
    }
}
