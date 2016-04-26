package connect4;

import java.io.StringWriter;
import java.util.ArrayList;

import connectionAPI.Player;

public class UtilityHeuristic {

	private GamePieces[][] m;
	private Player currentPlayer;
	private Player otherPlayer;
	
	public UtilityHeuristic(Connect4Game game) {
		Connect4Board board = (Connect4Board) game.getGameBoard();
		m = new GamePieces[board.boardHeight()][];
		for(int i =  0; i < board.boardHeight(); i++){
			m[i] = new GamePieces[board.boardWidth()];
			for(int j = 0; j < board.boardWidth(); j++){
				m[i][j] = board.getBoardSpace(i, j);
			}
		}
		currentPlayer = game.getPlayers().get(0).getMover((game.queryMove() - 2) % game.getPlayers().size());
		otherPlayer = currentPlayer == GamePieces.WHITE? GamePieces.BLACK: GamePieces.WHITE;
	}

	
	public Integer getHueristicUtility(){
		
		Integer utl = countColumnRuns(currentPlayer);
		utl+=countRowRuns(currentPlayer);
		utl+=countUpperLeftDiagonal(currentPlayer);
		utl+=countUpperRightDiagonal(currentPlayer);
		utl-=countColumnRuns(otherPlayer);
		utl-=countRowRuns(otherPlayer);
		utl-=countUpperLeftDiagonal(otherPlayer);
		utl-=countUpperRightDiagonal(otherPlayer);

		return utl;
	}
	
	private Integer countRowRuns(Player player){
		
		int h = m.length;
		int w = m[0].length;
		
		Integer c = 0;
		
		for(int i = 0; i < h; i++){
			GamePieces prev = GamePieces.EMPTY;
			for(int j = 0; j < w; j++){
//				System.out.println("prev="+prev);
//				System.out.println("m["+i+"]["+j+"]="+m[i][j]);
				if(m[i][j] == player && prev == GamePieces.EMPTY){
					prev = m[i][j];
				} else if(prev == m[i][j] && m[i][j] == player){
					c++;
					prev = m[i][j];
				} else {
					prev = GamePieces.EMPTY;
				}
			}
		}	
		return c;
	}
	
	private Integer countColumnRuns(Player player){
		
		int h = m.length;
		int w = m[0].length;
		
		Integer c = 0;
		
		for(int i = 0; i < w; i++){
			GamePieces prev = GamePieces.EMPTY;
			for(int j = 0; j < h; j++){
//				System.out.println("prev="+prev);
//				System.out.println("m["+j+"]["+i+"]="+m[j][i]);
				if(m[j][i] == player && prev == GamePieces.EMPTY){
					prev = m[j][i];
				} else if(prev == m[j][i] && m[j][i] == player){
					c++;
					prev = m[j][i];
				} else {
					prev = GamePieces.EMPTY;
				}
			}
		}	
		return c;
	}
	
	private Integer countUpperLeftDiagonal(Player player){
		int h = m.length;
		int w = m[0].length;
		Integer c = 0;
		GamePieces[][] d = new GamePieces[h][w + h - 1];
		
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				d[i][j] = GamePieces.EMPTY;
			}
		}
		
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				d[i][j + i] = m[i][j];
			}
		}
		
//		for(int i =  0; i < d.length; i++){
//			for(int j = 0; j < d[i].length; j++){
//				System.out.print(d[i][j] + " ");
//			}
//			System.out.println();
//		}
		
		
		int dh = d.length;
		int dw = d[0].length;
		
		for(int i = 0; i < dw; i++){
			GamePieces prev = GamePieces.EMPTY;
			for(int j = 0; j < dh; j++){
//				System.out.println("prev="+prev);
//				System.out.println("d["+j+"]["+i+"]="+d[j][i]);
				if(d[j][i] == player && prev == GamePieces.EMPTY){
					prev = d[j][i];
				} else if(prev == d[j][i] && d[j][i] == player){
					c++;
					prev = d[j][i];
				} else {
					prev = GamePieces.EMPTY;
				}
			}
		}	
		return c;
	}
	
	
	private Integer countUpperRightDiagonal(Player player){
		int h = m.length;
		int w = m[0].length;
		Integer c = 0;
		GamePieces[][] d = new GamePieces[h][w + h - 1];
		
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				d[i][j] = GamePieces.EMPTY;
			}
		}
		
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				d[i][j + h - i - 1] = m[i][j];
			}
		}
		
		
		int dh = d.length;
		int dw = d[0].length;
		
		for(int i = 0; i < dw; i++){
			GamePieces prev = GamePieces.EMPTY;
			for(int j = 0; j < dh; j++){
//				System.out.println("prev="+prev);
//				System.out.println("d["+j+"]["+i+"]="+d[j][i]);
				if(d[j][i] == player && prev == GamePieces.EMPTY){
					prev = d[j][i];
				} else if(prev == d[j][i] && d[j][i] == player){
					c++;
					prev = d[j][i];
				} else {
					prev = GamePieces.EMPTY;
				}
			}
		}	
		return c;
	}
	
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		for(int i =  0; i < m.length; i++){
			for(int j = 0; j < m[i].length; j++){
				sw.write(m[i][j] + " ");
			}
			if((m.length - 1) != i){
				sw.write(System.lineSeparator());
			}
		}
		return sw.toString();
	}
	
	
}
