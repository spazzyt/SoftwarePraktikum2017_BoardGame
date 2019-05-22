package ch.uzh.ifi.seal.soprafs17.web.websockets.messages;

/**
 * Created by nairboon on 16.04.17.
 */
public class WSMessage {
    public WSMessage() {
    }

    public WSMessage(MessageType type, Object newRoundMsg) {
        payload = newRoundMsg;
        this.type = type;
    }



    public enum MessageType {
        NOPE, OK, CONNECTTOGAME, USERCONNECTED, NEWROUND, CURRENTTURN, PLAYEDMOVE, ENDGAME, PICKCARD
    }

    public MessageType type;

    public Object payload;

    public WSMessage(MessageType t, String p) {
        type = t;
        payload = p;
    }
}
