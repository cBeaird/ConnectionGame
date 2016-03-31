package connect4;

import connectionAPI.Board;
import connectionAPI.GameBoard;
import connectionAPI.PlayerMove;
import connectionAPI.Strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class InteractiveStrategy implements Strategy {
    public static final String name = "Standard input interactive strategy";

    /**
     * @return name of strategy
     */
    @Override
    public String getStrategyName() {
        return name;
    }

    /**
     * The interactive strategy will ask the user to input their move from the given board
     * the move is entered by the tuple (hy, wx)
     *
     * @param board game board being played
     * @return the move that will be played
     */
    @Override
    public PlayerMove getNextMove(Board board) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int row, col;
        System.out.println();
        while (true) {
            System.out.println("Please select a move: eg.(HY, WX) ");
            try {
                String[] move = reader.readLine().split(",");
                for (int i = 0; i < move.length; i++)
                    move[i] = move[i].replaceAll("\\s", "").toUpperCase();

                if (move.length != 2) {
                    System.err.println("Illegal move please try again!");
                    continue;
                }

                if (move[0].startsWith(GameBoard.HEIGHT_CORDINATE)) {
                    row = Integer.parseInt(move[0].substring(1));
                    col = Integer.parseInt(move[1].substring(1));
                } else if (move[0].startsWith(GameBoard.WIDTH_CORDINATE)) {
                    row = Integer.parseInt(move[1].substring(1));
                    col = Integer.parseInt(move[0].substring(1));
                } else {
                    System.err.println("Invalid input please try again!");
                    continue;
                }

                if ((row < 0) || (col < 0)) {
                    System.err.println("Illegal move please try again!");
                    continue;
                }

                Connect4Move m = new Connect4Move(row, col);
                if (!board.getLegalMoves().containsKey(m.hashCode())) {
                    System.err.println("Illegal move please try again!");
                    continue;
                }

                return m;
            } catch (IOException e) {
                System.err.println("Invalid input please try again!");
            }
        }
    }
}