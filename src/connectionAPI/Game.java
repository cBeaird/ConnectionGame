package connectionAPI;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public abstract class Game {
    protected Board gameBoard;
    protected ArrayList<Player> players;
    protected AtomicInteger moveNumber;

    public Game() {
        moveNumber = new AtomicInteger(0);
    }

    public Game(Board gameBoard, ArrayList<Player> players) {
        this.gameBoard = gameBoard;
        this.players = players;
        moveNumber = new AtomicInteger(0);
    }

    /**
     * this is move and get ready for the next player
     *
     * @return the current move
     */
    public int moveNumber() {
        return moveNumber.getAndIncrement();
    }

    /**
     * query the move number with out making a play
     *
     * @return int of current move number
     */
    public int queryMove() {
        return moveNumber.get();
    }

    /**
     * @return my game board
     */
    public Board getGameBoard() {
        return this.gameBoard;
    }

    /**
     * @return list of players playing the game
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * This method is intended to be used with copy there is no other reason to change the move
     *
     * @param move int for current move
     */
    public void setMoveNumber(int move) {
        this.moveNumber.set(move);
    }

    public int hashCode() {
        return this.getGameBoard().hashCode() + (73 * this.queryMove());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;

        Game game = (Game) o;

        if (!getGameBoard().equals(game.getGameBoard())) return false;
        if (!getPlayers().equals(game.getPlayers())) return false;
        return moveNumber.equals(game.moveNumber);

    }

    /**
     * Main Driver for playing a game
     */
    public abstract void play();


    /**
     * @return deep copy of the game
     */
    public abstract Game copy();
}
