package tests;

import connect4.Connect4Board;
import connect4.Connect4Game;
import connect4.GamePieces;
import connect4.SmarterRandomStrategy;
import connectionAPI.Player;

import java.util.ArrayList;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class Connect4Test2 {
    public static void main(String args[]) {
        Connect4Game c4g;
        ArrayList<Player> players = new ArrayList<>();

        for (Player p : GamePieces.values()) {
            if (p == GamePieces.EMPTY)
                continue;
            if (p.turn() == 1)
                p.setPlayerStrategy(new SmarterRandomStrategy());
            if (p.turn() == 0)
                p.setPlayerStrategy(new SmarterRandomStrategy());
            players.add(p);
        }

        // build a new game with players
        c4g = new Connect4Game(6, 7, players);
        ((Connect4Board) c4g.getGameBoard()).setConnectionLength(2);
        c4g.play();

        System.exit(0);
    }
}
