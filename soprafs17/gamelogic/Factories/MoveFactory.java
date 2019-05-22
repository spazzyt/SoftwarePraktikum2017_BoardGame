package ch.uzh.ifi.seal.soprafs17.gamelogic.Factories;

import ch.uzh.ifi.seal.soprafs17.gamelogic.Player;
import ch.uzh.ifi.seal.soprafs17.entity.Move;

import java.util.List;

/**
 * Created by nairboon on 01.04.17.
 */
public interface MoveFactory {
    public class MoveFactoryException extends Exception {}

    public List<Move> createMoves(Player p);
}
