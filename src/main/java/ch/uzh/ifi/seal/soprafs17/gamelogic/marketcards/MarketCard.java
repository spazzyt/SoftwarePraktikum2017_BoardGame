package ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards;

/**
 * Created by Spasen on 19.04.17.
 */
public class MarketCard {

    public enum Type { Statue, Blue, Green, Red};
/*
*

 RED CARDS
- Paved Path (2) -> Obelisk
- Sarcophagus (2) -> BurialChamber
- Entrance (2) -> Pyramide

GREEN CARDS
- Pyramid Decoration (2)
- Temple Decoration (2)
- Burial Chamber Decoration (2)
- Obelisk Decoration (2)

VIOLET CARD
- Statue (10)

BLUE CARDS
- Chisel (3)
- Lever (2)
- Hammer (2)
- Sail (3)
 *  */

    public Type type;
    public Integer id;

    public static final int gimmepoints = 1;

    public MarketCard(Type t, int cardid){
        this.type = t;
        this.id = cardid;
    }

}
