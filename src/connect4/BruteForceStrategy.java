package connect4;

import connectionAPI.Game;
import connectionAPI.Player;
import connectionAPI.PlayerMove;
import connectionAPI.Strategy;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class BruteForceStrategy implements Strategy {
    public static final String name = "Brute force full tree search strategy";

    /**
     * @return the name in plane english for the strategy
     */
    @Override
    public String getStrategyName() {
        return BruteForceStrategy.name;
    }

    /**
     * This allows for different strategies to be played by different players
     *
     * @param game board being played
     * @return a move object
     */
    @Override
    public PlayerMove getNextMove(Game game) {
        Player currentPlayer = game.getPlayers().get(0).getMover((game.queryMove() - 1) % game.getPlayers().size());
        int bestValue = Integer.MIN_VALUE;
        PlayerMove bestMove = null;

        for (PlayerMove m : game.getGameBoard().getLegalMoves().values()) {
            Connect4Game hypotheticalGame = (Connect4Game) game.copy();

            m.setOwner(hypotheticalGame.getPlayers().get(0).getMover((hypotheticalGame.queryMove() - 1) % hypotheticalGame.getPlayers().size()));
            ((Connect4Board) hypotheticalGame.getGameBoard()).setBoardSpace(m.getYCoordinate(), m.getXCoordinate(), (GamePieces) m.getOwner());
            if (((Connect4Board) hypotheticalGame.getGameBoard()).isWon())
                return m;

            int value = getValueOfMove(hypotheticalGame, currentPlayer);
            bestMove = value > bestValue ? m : bestMove;
        }
        return bestMove;
    }

    private int getValueOfMove(Game copiedGame, Player currentPlayer) {
        int value = 0;

        for (int row = 0; row < copiedGame.getGameBoard().boardHeight(); row++) {
            for (int col = 0; col < copiedGame.getGameBoard().boardWidth(); col++) {
                if (((Connect4Board) copiedGame.getGameBoard()).getBoardSpace(row, col) == GamePieces.EMPTY) {
                    Game nextGame = copiedGame.copy();
                    Player p = nextGame.getPlayers().get(0).getMover(nextGame.queryMove() % nextGame.getPlayers().size());
                    ((Connect4Board) nextGame.getGameBoard()).setBoardSpace(row, col, (GamePieces) p);

                    switch (((Connect4Board) nextGame.getGameBoard()).getWinner().visualization()) {
                        case ' ':
                            if (((Connect4Board) nextGame.getGameBoard()).isFinished(nextGame.queryMove())) {
                                value += 0;
                            } else {
                                nextGame.moveNumber();
                                value += getValueOfMove(nextGame, currentPlayer);
                            }
                            break;
                        case 'X':
                            value += currentPlayer.visualization() == 'X' ? 1 : -1;
                            break;
                        case 'O':
                            value += currentPlayer.visualization() == 'O' ? 1 : -1;
                            break;
                    }
                }
            }
        }
        return value;
    }
}
