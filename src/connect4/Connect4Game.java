package connect4;

import connectionAPI.*;

import java.util.ArrayList;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class Connect4Game extends Game {
    public Connect4Game() {
        super();
    }

    public Connect4Game(int height, int width, ArrayList<Player> players) {
        super(new Connect4Board(height, width), players);
    }

    public Connect4Game(Board board, ArrayList<Player> players) {
        super(board, players);
    }

    /**
     * Void play method main driver for a game
     */
    @Override
    public void play() {
        while (true) {
            this.getGameBoard().display();
            // Check to see if the game is finished
            if (((Connect4Board) getGameBoard()).isFinished(this.queryMove())) {
                if (((Connect4Board) getGameBoard()).getWinner() != GamePieces.EMPTY) {
                    System.out.println(String.format("Winner is %c", ((Connect4Board) getGameBoard()).getWinner().visualization()));
                    break;
                } else {
                    System.out.println("Tie game no winner");
                    break;
                }
            }

            // Check to see if there is a winner
            if (((Connect4Board) getGameBoard()).getWinner() != GamePieces.EMPTY) {
                System.out.println(String.format("Winner is %c", ((Connect4Board) getGameBoard()).getWinner().visualization()));
                break;
            }

            Connect4Board b = (Connect4Board) this.getGameBoard();
            GamePieces p = (GamePieces) this.players.get(0).getMover(this.moveNumber() % 2);
            Strategy s = p.getPlayerStrategy();
            PlayerMove m = s.getNextMove(this);
            b.setBoardSpace(m.getYCoordinate(), m.getXCoordinate(), p);

            System.out.println(String.format("Move number %d player %c", this.queryMove(), p.visualization()));
        }
    }

    /**
     * @return Copied Game object
     */
    @Override
    public Game copy() {
        Connect4Game copiedGame = new Connect4Game(this.getGameBoard().copy(), this.getPlayers());
        copiedGame.setMoveNumber(this.queryMove());
        return copiedGame;
    }

    public void playSilent() {
        while (true) {
            // Check to see if the game is finished
            if (((Connect4Board) getGameBoard()).isFinished(this.queryMove())) {
                if (((Connect4Board) getGameBoard()).getWinner() != GamePieces.EMPTY) {
                    break;
                } else {
                    break;
                }
            }

            // Check to see if there is a winner
            if (((Connect4Board) getGameBoard()).getWinner() != GamePieces.EMPTY) {
                break;
            }

            Connect4Board b = (Connect4Board) this.getGameBoard();
            GamePieces p = (GamePieces) this.players.get(0).getMover(this.moveNumber() % 2);
            Strategy s = p.getPlayerStrategy();
            PlayerMove m = s.getNextMove(this);
            b.setBoardSpace(m.getYCoordinate(), m.getXCoordinate(), p);

        }
    }
}
