package ch.uzh.ifi.seal.soprafs17.gamelogic;

import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.Action;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SailAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.SledToShipAction;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.BlueMarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.GreenMarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.MarketCard;
import ch.uzh.ifi.seal.soprafs17.gamelogic.marketcards.StatueMarketCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by nairboon on 21.04.17.
 */
public class Market extends OriginAndDepartingSite {

    private final Logger log = LoggerFactory.getLogger(Market.class);

    public boolean boo = false;

    public List<MarketCard> allCards = new ArrayList<>();

    // 1st deck
    public List<MarketCard> availableDeck= new ArrayList<>();


    // 2nd deck (shown to the players)
    public List<MarketCard> pepeCards = new ArrayList<>();

    // 3th deck = Ablagestapel
    public List<MarketCard> playedDeck = new ArrayList<>();


    public Stack<PlayerFieldColour> colorStack = new Stack<>();

    private  int cardId = 0;

    // do we wait for a multi-move card thing?
    public int comboMove = 0;

    @JsonIgnore
    public TurnManager tm;


    public void initDemCardsYo(Pyramid p, Obelisk o, BurialChamber b, Temple t) {

        /*
          id 0-9 is statue and
          10-11 is green-Pyramid 12-13 is green-obelisk 14-15 is green-burialChamber 16-17 green-temple
          red 18-19 paved path, 20-21 sarkophag, 22-23 entrance
          blue 24-26 chisel, 27-28 lever, 29-30 hamer, 31-33 sails
        */
        // 1. fill allCards
        for(int i=0;i<10;i++) {
            cardId = i;
            allCards.add(new StatueMarketCard(cardId));
        }
        cardId++;


        //we pro: 2x pyramid
        allCards.add(new GreenMarketCard(cardId++, p));
        allCards.add(new GreenMarketCard(cardId++, p));
        //2x obelisk
        allCards.add(new GreenMarketCard(cardId++, o));
        allCards.add(new GreenMarketCard(cardId++, o));
        //2x burialChamber
        allCards.add(new GreenMarketCard(cardId++, b));
        allCards.add(new GreenMarketCard(cardId++, b));
        //2x temple yo
        allCards.add(new GreenMarketCard(cardId++, t));
        allCards.add(new GreenMarketCard(cardId++, t));

        //red 18-19 paved path, 20-21 sarkophag, 22-23 entrance
        for(int i=0;i<6;i++)
            allCards.add(new MarketCard(MarketCard.Type.Red, cardId++));


        SailAction sa = null;
        SledToShipAction ssa = null;
        for(Action a : tm.actions) {
            if (a.type == Action.Type.Sail) {
                sa = (SailAction) a;
            } else if (a.type == Action.Type.Sled) {
                ssa = (SledToShipAction) a;
            }
        }

        //blue 24-26 chisel, 27-28 lever, 29-30 hamer, 31-33 sails


        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Chisel, cardId++, sa, ssa));
        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Chisel, cardId++, sa, ssa));
        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Chisel, cardId++, sa, ssa));

        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Lever, cardId++, sa, ssa));
        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Lever, cardId++, sa, ssa));

        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Hammer, cardId++, sa, ssa));
        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Hammer, cardId++, sa, ssa));

        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Sail, cardId++, sa, ssa));
        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Sail, cardId++, sa, ssa));
        allCards.add(new BlueMarketCard(BlueMarketCard.BlueType.Sail, cardId++, sa, ssa));



        // 2. all to availableCards
        for(MarketCard mc : allCards)
            availableDeck.add(mc);

        Collections.shuffle(availableDeck);
    }

    @Override
    public void onShipDocked() {

        for(int i=this.ship.numFields-1; i >= 0;i--) {
            if( this.ship.fields[i] != PlayerFieldColour.EMPTY.ordinal() )
                colorStack.push(PlayerFieldColour.values()[this.ship.fields[i]]);
        }
    }

    @Override
    public void setStone(PlayerFieldColour c) {
        boo = true;
    }

    public void newRound() {

        // take 4 from availableDeck to pepeCards unless availableDeck.size < 4 -> shuffle playedDeck into availableDeck
        if(!pepeCards.isEmpty()) { // 2nd deck (shown to the players)
            playedDeck.addAll(pepeCards);
            pepeCards.clear();
        }

        //standard: does update pepeCards and removes them from availableDeck
        int toTake = Math.min(4, availableDeck.size());
        if(toTake > 0) {
            List<MarketCard> taken = availableDeck.subList(0, toTake);
            pepeCards.addAll(taken);
            availableDeck.removeAll(taken);
        }

    }


    @Override
    public boolean validateMoveImplementation(Move m) {
        //pick up card move todo works for now only for statue
        if(m.from.equals("Market") && m.to.equals("PlayerCardStack")){
            for(MarketCard mc : pepeCards){
                if(m.pos.equals(mc.id)){
                    return true;
                }
            }
            return false;

        //play card move
        }else if(m.from.equals("PlayerCardStack") && m.to.equals("Market")){

            return true;
        } else if(m.from.equals("DepartingHarbour") && m.to.equals("Market")){
            if(ship != null)
                return false;

            return true;
        }


        // check if card is on pepeDeck

        return false;
    }


    //aufnehmen
    @Override
    public boolean executeMoveImplementation(Move m) {
        if(m.from.equals("Market") && m.to.equals("PlayerCardStack")) {
            for(MarketCard mc : pepeCards){
                if(m.pos.equals(mc.id)) {

                    // give stone back!!
                    m.getUser().player.quarry.stonesLeft += 1;


                    if(mc.type == MarketCard.Type.Red) {

                        // play the card

                        //red 18-19 paved path, 20-21 sarkophag, 22-23 entrance

                        Move mov = null;
                        switch (mc.id) {

                            case 18:
                            case 19:
                                // paved
                                mov = new Move("Quarry", "Obelisk", 1);
                                break;

                            case 20:
                            case 21:
                                // sarkoding
                                mov = new Move("Quarry", "BurialChamber", 1);
                                break;

                            case 22:
                            case 23:
                                // entrance
                                mov = new Move("Quarry", "Pyramid", 1);
                                break;

                        }

                        if( mov != null) {
                            mov.setUser(m.getUser());
                            tm.executeRedMove(mov);
                        }


                        // yolo

                        playedDeck.add(mc);
                    } else {
                        m.getUser().player.marketCards.add(mc);
                    }
                    pepeCards.remove(mc);
                    return true;
                }
            }
            return false;
        }else if(m.from.equals("PlayerCardStack") && m.to.equals("Market")){
            for(MarketCard mc : allCards){
                if(m.pos.equals(mc.id)){
                    playedDeck.add(mc);


                    if(mc.type == MarketCard.Type.Blue) {

                        BlueMarketCard bmc = (BlueMarketCard) mc;

                        // give it the market for swag
                        bmc.market = this;

                        log.debug("playing a blue card: "+ mc.id);

                        if(bmc.isPlayable(m.getUser().player)) {
                            if(!bmc.playCard(m))
                                return false;

                            comboMove = bmc.comboMove;
                        } else {
                            return false;
                        }

                    }



                    return true;
                }
            }
            return false;
        }
        return true;
    }


    public PlayerFieldColour nextPlayerToPick() {
        return colorStack.pop();
    }

    @Override
    public int assessPoints(PlayerFieldColour c) {
        return 0;
    }


}