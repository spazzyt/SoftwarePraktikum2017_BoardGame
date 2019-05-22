package ch.uzh.ifi.seal.soprafs17.web.websockets.messages;

import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Spasen on 18.04.17.
 */
public class PlayedMoveMessage {

    public String player;
    public Move move;

    // scores für alli noobs
    public Map<String,Integer> berlinerScore = new HashMap<>();

    public Map<String,Integer> quarries = new HashMap<>();
    public Map<String,Integer> sleds = new HashMap<>();

    // cards für alli
    public Map<String, List<MarketCard>> youMad = new HashMap<>();
}
