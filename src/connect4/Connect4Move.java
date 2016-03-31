package connect4;

import connectionAPI.ConnectionGameException;
import connectionAPI.Player;
import connectionAPI.PlayerMove;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class Connect4Move implements PlayerMove {
    // a move as defined by the conceptual definition of the board in this implementation.
    private int column; // x axis
    private int row;    // y axis
    private Player owner;

    Connect4Move(int row, int column) {
        this.column = column;
        this.row = row;
    }

    /**
     * @return int for the x axis for the move
     */
    @Override
    public int getXCoordinate() {
        return this.column;
    }

    /**
     * @return int y axis for the move
     */
    @Override
    public int getYCoordinate() {
        return this.row;
    }

    /**
     * @return exception obj this game does not support 3d play
     */
    @Override
    public int getZCoordinate() {
        throw new ConnectionGameException("This game does not allow 3D play");
    }

    /**
     * @return returns null or this player occupying this board space
     */
    @Override
    public Player getOwner() {
        return this.owner;
    }

    /**
     * @param player sets player occupying the board space idetified by the move
     */
    @Override
    public void setOwner(Player player) {
        if (!(player instanceof GamePieces)) {
            throw new ConnectionGameException(String.format("%s is not a valid player", player.getClass().getName()));
        }
        this.owner = player;
    }

    @Override
    public int hashCode() {
        return this.owner == null ? ((this.row * 17) + (this.column * 31)) :
                ((this.row * 17) + (this.column * 31) + this.owner.getPlayerHashID());
    }
}
