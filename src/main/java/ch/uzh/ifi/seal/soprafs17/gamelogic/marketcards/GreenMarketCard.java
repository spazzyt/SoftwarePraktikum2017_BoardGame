package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards;

import ch.uzh.ifi.seal.soprafs17.gamelogic.DestinationSite;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Spasen on 19.04.17.
 */
public class GreenMarketCard extends MarketCard{

    @JsonIgnore
    private DestinationSite destinationSite;

    public GreenMarketCard(int card_id, DestinationSite ds) {
        super(Type.Green, card_id);
        this.destinationSite = ds;
    }

    public int gimmepoints(){
        return destinationSite.getGreenCount();
    }
}
