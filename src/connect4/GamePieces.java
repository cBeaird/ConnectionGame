package connect4;

import connectionAPI.Player;
import connectionAPI.Strategy;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public enum GamePieces implements Player {
    EMPTY(' ', -1, 2),
    WHITE('X', 0, 7417),
    BLACK('O', 1, 7573);

    private final char displayChar;
    private final int moveOnTurn;
    private final int hashID;
    private Strategy strategy = null;

    /**
     * @param visualization display character for printing the board to stdout
     * @param hashID        Unique prime for each player
     */
    GamePieces(char visualization, int turn, int hashID) {
        this.displayChar = visualization;
        this.moveOnTurn = turn;
        this.hashID = hashID;
    }

    /**
     * @return character that is used to represent the player when stdout is used
     */
    @Override
    public char visualization() {
        return displayChar;
    }

    /**
     * @return a fixed value for the player who plays first in the game
     */
    @Override
    public GamePieces movesFirst() {
        return WHITE;
    }

    /**
     * Each player must have a unique moveOnTurn [0, 1, 2, ..., n]; |n| = the number of players
     * turn can then be calculated by ((current play) % (number of players)) getMover will return the correct player
     *
     * @return the move the player makes
     */
    @Override
    public int turn() {
        return moveOnTurn;
    }

    /**
     * @param playerStrategy reference to the players strategy
     */
    @Override
    public void setPlayerStrategy(Strategy playerStrategy) {
        this.strategy = playerStrategy;
    }

    /**
     * @return strategy played by the player
     */
    @Override
    public Strategy getPlayerStrategy() {
        return strategy;
    }

    /**
     * @param i player with moveOnTurn == i
     * @return player
     */
    @Override
    public Player getMover(int i) {
        for (GamePieces p : GamePieces.values()) {
            if (p.turn() == i)
                return p;
        }
        return null;
    }

    /**
     * hashCode() is final in Enum and cannot be overridden so returns the unique hash key assigned to each player
     *
     * @return hashCode()
     */
    @Override
    public int getPlayerHashID() {
        return this.hashID;
    }
}