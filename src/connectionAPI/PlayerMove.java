package connectionAPI;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public interface PlayerMove {
    /**
     * @return integer representing the X coordinate of a standard orientation 3 dimensional cube (horizontal plane)
     */
    int getXCoordinate();

    /**
     * @return integer representing the Y coordinate of a standard orientation 3 dimensional cube (vertical plane)
     */
    int getYCoordinate();

    /**
     * @return integer representing the Z coordinate of a standard orientation 3 dimensional cube (depth plane)
     */
    int getZCoordinate();

    /**
     * @return player occupying the current board space
     */
    Player getOwner();

    /**
     * @param player the player that holds the current space
     */
    void setOwner(Player player);
}
