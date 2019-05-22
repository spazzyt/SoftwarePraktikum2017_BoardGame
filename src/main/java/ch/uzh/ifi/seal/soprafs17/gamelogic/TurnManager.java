package ch.uzh.ifi.seal.soprafs17.gamelogic;


import ch.uzh.ifi.seal.soprafs17.constant.PlayerFieldColour;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.gamelogic.actions.*;
import ch.uzh.ifi.seal.soprafs17.web.websockets.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnManager {

    private final Logger log = LoggerFactory.getLogger(TurnManager.class);
    public Game game;

    public User activePlayer;
    private int round = -1;

    private int playerTurnCount = -1;

    public Map<String,SiteBoard> siteBoards;

    public List<DockableSite> ds = new ArrayList<>();

    public List<Action> actions = new ArrayList<>();

    private RoundCard roundCard;


    public TurnManager(Map<String,SiteBoard> sb, Game game) {
        siteBoards = sb;
        this.game = game;


        ds.add((DockableSite) siteBoards.get("Market"));

        ds.add((DockableSite) siteBoards.get("Obelisk"));
        ds.add((DockableSite) siteBoards.get("Temple"));

        ds.add((DockableSite) siteBoards.get("Pyramid"));
        ds.add((DockableSite) siteBoards.get("BurialChamber"));


        Market market = (Market) siteBoards.get("Market");
        market.tm = this;

        DepartingHarbour dh = (DepartingHarbour) siteBoards.get("DepartingHarbour");
        SailAction sailsAction = null;
        QuarryToSledAction quarryToSledAction = null;
        SledToShipAction sledToShipAction = null;
        PickupCardAction pickupCardAction = null;
        PlayCardAction playCardAction = new PlayCardAction();


            sailsAction = new SailAction(dh, ds);

            quarryToSledAction = new QuarryToSledAction();

            sledToShipAction = new SledToShipAction(dh);

            pickupCardAction = new PickupCardAction((Market) siteBoards.get("Market"));


        actions.add(sailsAction);
        actions.add(sledToShipAction);
        actions.add(quarryToSledAction);

        actions.add(pickupCardAction);

        actions.add(playCardAction);

        roundCard = new RoundCard(game.getNumPlayers());
    }

    public void startRound() {
        calcRoundScore();
        round++;

        // we are done
        if(round >= 6) {
            gameOver();
            return;
        }
        DepartingHarbour dh = (DepartingHarbour) siteBoards.get("DepartingHarbour");
        dh.setSizeOfShips(roundCard.getActiveArray().get(round));

        // check if any old ship is still docked
        for(DockableSite site : ds)
            site.clearShip();

        Market mc = (Market) siteBoards.get("Market");
        mc.newRound();

        NewRoundMessage newRoundMsg = new NewRoundMessage(round);

        for(User pepe: game.players) {
            newRoundMsg.berlinerScore.put(pepe.getUsername(), pepe.player.score);
            newRoundMsg.quarries.put(pepe.getUsername(), pepe.player.quarry.stonesLeft);
            newRoundMsg.sleds.put(pepe.getUsername(), pepe.player.sled.sledLoad);

        }

        for(Integer i: dh.ships.keySet())
            newRoundMsg.ships.add(dh.ships.get(i));

        newRoundMsg.setMarketCards(mc.pepeCards);

        WSMessage msg = new WSMessage(WSMessage.MessageType.NEWROUND, newRoundMsg);
        game.broadcast(msg);


        // message activePlayer that its's his turn
        nextPlayer();
    }

    private void gameOver() {
        log.debug("Game is over");

        calcEndScore();

        EndGameMessage em = new EndGameMessage();

        for(User pepe: game.players) {
            em.finalScores.put(pepe.getUsername(), pepe.player.score);
            em.finalScoreDumbArray.add(pepe.player.score);
        }

        WSMessage msg = new WSMessage(WSMessage.MessageType.ENDGAME, em);
        game.broadcast(msg);

        // remove all decisions
        for(User u : game.players)
            u.player.decision = null;


        game.gameOver();
    }

    public void nextPlayer() {

        // time for next round = öb alli schiff weg sind


        if( ++playerTurnCount > game.getNumPlayers()-1) {
            playerTurnCount = 0;
        }


            User ap = game.players.get(playerTurnCount);
            setActiveUser(ap);
            activePlayer.player.decision = createStandardDecision(activePlayer.player);

             CurrentTurnMessage ctm = new CurrentTurnMessage();
             ctm.user = ap.getUsername();

            WSMessage msg = new WSMessage(WSMessage.MessageType.CURRENTTURN, ctm);
            game.broadcast(msg);

    }

    /*
     * Sets the player and calculate his possible moves
     * gamelooptest
     * turnmanager
     * change setActivePlayer(Player p) -> setActiveUser(User p)
     */
    public void setActiveUser(User p) {
        // kill previous noob
        if(activePlayer != null)
            activePlayer.player.decision = null;

        activePlayer = p;

        siteBoards.put("Sled", p.player.sled);
        siteBoards.put("Quarry", p.player.quarry);
        siteBoards.put("PlayerCardStack", p.player.cardStack);

    }

    public Decision createStandardDecision(Player p) {

        Decision d = new Decision();

        for(Action a : actions) {

            int nMoves = a.generateValidMoves(p);
            if(nMoves > 0) {
                d.actions.add(a);
            }
        }

        return d;
    }

    private Decision createMarketDecision(Player p) {

        Decision d = new Decision();

         PickupCardAction a = new PickupCardAction((Market) siteBoards.get("Market"));
            int nMoves = a.generateValidMoves(p);
            if(nMoves > 0) {
                d.actions.add(a);
            }

        return d;
    }

    public boolean executeRedMove(Move m) {

        if( siteBoards.containsKey(m.from) && (siteBoards.containsKey(m.to))) {

            SiteBoard origin = siteBoards.get(m.from); //Market
            SiteBoard target = siteBoards.get(m.to);

            if( origin.validateMove(m) && target.validateMove(m) ) {
                m.toSite = target;

                return executeAndBroadcast(m, origin, target);
            }
            return false;
        }
        return false;
    }

    public boolean executePlayerMove(Move m) {

        // check it in usage if null or not null
        Market mc = (Market) siteBoards.get("Market");


        if( siteBoards.containsKey(m.from) && (siteBoards.containsKey(m.to))) {

            SiteBoard origin = siteBoards.get(m.from); //Market
            SiteBoard target = siteBoards.get(m.to);   //PlayerCardStack

            // TODO: concurency
            if( origin.validateMove(m) && target.validateMove(m) ) {
                m.toSite = target;



                boolean status = executeAndBroadcast(m, origin, target);

                if(!status) {
                    log.error("execute Failed?: " + m);
                    return false;
                }

                // special case
                if(origin.siteId.equals("DepartingHarbour") && target.siteId.equals("Market")) {
                    // 1. get "first" player according to ship
                    // 2. create marketcarddecision
                    // broadcast WS msg
                    //
                    nextMarketPick((Market) target);

                    //aufnehmen
                } else if( origin.siteId.equals("Market") && target.siteId.equals("PlayerCardStack")) {
                   // MarketCard theCard = activePlayer.player.gimmeCard(m.pos);
                    //mc.playedDeck.add(theCard);






                    if(mc.colorStack.size() > 0) {
                        nextMarketPick(mc);
                    } else {
                        checkForNextRound(mc);
                    }

                }
                /*else if( origin.siteId.equals("PlayerCardStack") && target.siteId.equals("Market")) {

                } */
                else {
                    // does the user have a card he is currently playing?
                    if(activePlayer.player.playingCard != null) {
                        if (
                            // is he done yet with the multi move
                                !activePlayer.player.playingCard.donePlaying()) {
                            log.debug("Combomove > 0....");
                            // wait for the next move???

                        /* not done yet
                        *  - set user's decision to the next action of the card
                         */

                            activePlayer.player.decision.actions.clear();

                            Action cardAction = activePlayer.player.playingCard.step(m);

                            /* if (cardAction.moves == null || cardAction.moves.size() == 0)
                                log.error("CardAction has no moves??: " + cardAction);
                            */

                            log.debug("user got next action in combo: " + cardAction);
                            activePlayer.player.decision.actions.add(cardAction);


                            log.debug("Status is: " + status);


                        } else {
                            log.debug("Combomove == 0: check for next");
                            // remove it
                            activePlayer.player.playingCard = null;
                            checkForNextRound(mc);
                        }
                        return status;
                    }


                   /* if(mc.comboMove > 0) {
                        log.debug("this is a combomove??");
                        mc.comboMove--;
                    }
                    */

                        // check if the round is finished
                        checkForNextRound(mc);


                }

                // is this the right place for this check?

                return status;
            } else {
                log.error("ValidateMove failed");
            }


        }
        /*else {
            log.error("executePlayerMove siteboard does not contain from & to of this move");
        }*/

        return false;
    }

    private void checkForNextRound(Market mc)  {
        DepartingHarbour dh = (DepartingHarbour) siteBoards.get("DepartingHarbour");
        //mc.colorStack.size() does not have to be bigger than 0
        if (dh.ships.size() == 0 && mc.colorStack.size() == 0) {
            log.debug("There is no ship in the harbour: next round");
            startRound();

        } else {
            this.nextPlayer();
        }
    }

    public boolean executeAndBroadcast(Move m, SiteBoard origin, SiteBoard target) {
        boolean ostat = origin.executeMove(m);
        boolean tstat =  target.executeMove(m);
        boolean status = (ostat && tstat);

        if(!ostat)
            log.error("OriginExecute Failed!");

        if(!tstat)
            log.error("TargetExecute Failed!");


        if(!status)
            return false;

        if(target.siteId.equals("Pyramid"))
            calcInstaScore();

        broadcastMove(m);

        return status;
    }

    public void broadcastMove(Move m) {
        PlayedMoveMessage playedMoveMsg = new PlayedMoveMessage();
        playedMoveMsg.move = m;
        playedMoveMsg.player = activePlayer.getUsername();

        for(User pepe: game.players) {
            playedMoveMsg.quarries.put(pepe.getUsername(), pepe.player.quarry.stonesLeft);
            playedMoveMsg.sleds.put(pepe.getUsername(), pepe.player.sled.sledLoad);
            playedMoveMsg.youMad.put(pepe.getUsername(), pepe.player.marketCards);
        }


        for(User pepe: game.players) {
            playedMoveMsg.berlinerScore.put(pepe.getUsername(), pepe.player.score);
        }

        WSMessage msg = new WSMessage(WSMessage.MessageType.PLAYEDMOVE, playedMoveMsg);

        game.broadcast(msg);
    }

    private void nextMarketPick(Market target)  {
        PlayerFieldColour firstPlayerColour = target.nextPlayerToPick();
        // color.ordinal -1 => player id, because of EMPTY = 0
        User firstPlayer = game.players.get(firstPlayerColour.ordinal()-1); // <-- dreckscheib

        setActiveUser(firstPlayer);
        activePlayer.player.decision = createMarketDecision(firstPlayer.player);

        PickCardMessage pcm = new PickCardMessage();
        pcm.user = firstPlayer.getUsername();

        WSMessage pmsg = new WSMessage(WSMessage.MessageType.PICKCARD, pcm);
        game.broadcast(pmsg);
    }


    //tempel am ende der runde
    //obelsik grabkammer ende des spiels
    //pyramide sofort
    public void calcInstaScore() {
        for(User pepe: game.players) {
            pepe.player.score += siteBoards.get("Pyramid").assessPoints(pepe.player.colour);
        }
    }

    private void calcRoundScore() {
        for(User pepe: game.players) {
            pepe.player.score += siteBoards.get("Temple").assessPoints(pepe.player.colour);
        }
    }
    private void calcEndScore() {
        for(User pepe: game.players) {
            pepe.player.score += siteBoards.get("Obelisk").assessPoints(pepe.player.colour);
            pepe.player.score += siteBoards.get("BurialChamber").assessPoints(pepe.player.colour);
            //
        }
    }
}
