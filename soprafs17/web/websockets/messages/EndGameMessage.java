package ch.uzh.ifi.seal.soprafs17.web.websockets.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nairboon on 26.04.17.
 */
public class EndGameMessage {
    public Map<String,Integer> finalScores = new HashMap<>();
    public List<Integer> finalScoreDumbArray = new ArrayList<>();
}
