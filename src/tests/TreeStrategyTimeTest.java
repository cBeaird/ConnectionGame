package tests;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import connect4.Connect4Board;
import connect4.Connect4Game;
import connect4.GamePieces;
import connect4.RandomStrategy;
import connect4.SmarterRandomStrategy;
import connect4.TreeSearchStrategy;
import connectionAPI.GameBoard;
import connectionAPI.Player;
import connectionAPI.Strategy;

public class TreeStrategyTimeTest {
	
	public static void main(String[] args) {
		
		Map<String, List<List<Long>>> results = new HashMap<>();
		
		int h[] =  new int[]{4,5,6,7,8,9};
		int w[] =  new int[]{5,6,7,8,9,10};
		
		List<Strategy> myStrats = new ArrayList<>();
		myStrats.add(new TreeSearchStrategy(2));
		myStrats.add(new TreeSearchStrategy(4));
     	myStrats.add(new TreeSearchStrategy(6));
//		myStrats.add(new TreeSearchStrategy(8));

		for(int k =0; k < h.length; k++){
			for(int i = 0; i < myStrats.size(); i++){
				for(int j=0; j<50; j++){
					Connect4Game c4g;
					ArrayList<Player> players = new ArrayList<>();

					Strategy strategy = myStrats.get(i);
					
					for (Player p : GamePieces.values()) {
						if (p == GamePieces.EMPTY)
							continue;
						if (p.turn() == 1)
							p.setPlayerStrategy(new RandomStrategy());
						if (p.turn() == 0)
							p.setPlayerStrategy(strategy);
						players.add(p);
					}
					// build a new game with players
					c4g = new Connect4Game(h[k], w[k], players);

					((Connect4Board) c4g.getGameBoard()).setConnectionLength(4);
					c4g.playSilent();
					
					String key = strategy.getStrategyName() + " ("+ h[k] +"x"+ w[k]+")";
					
					if(!results.containsKey(key)){
						results.put(key, new ArrayList<List<Long>>());
					}
					results.get(key).add(((TreeSearchStrategy)strategy).getGetMoveDurations());

				}

			}
		}
		
		for(String key: results.keySet()){
			int maxSize = -1;
			for(List<Long> times: results.get(key)){
				if(times.size() > maxSize){
					maxSize = times.size();
				}
			}
			
			
			for(List<Long> times: results.get(key)){
				while(times.size() < maxSize){
					times.add(0L);
				}
			}

			
			List<Double> avg = average(results.get(key));
			
			String as = avg.toString();
			as = as.substring(1);
			as = as.substring(0, as.length() - 1);
			
			System.out.println(key + ", " + as);
			
			
			
		}
	
	}
	
	private static List<Double> average(List<List<Long>> runs){
		List<List<Long>> cols = new ArrayList<>();

		int maxSize = -1;
		for(List<Long> list: runs){
			if(list.size() > maxSize){
				maxSize = list.size();
			}
		}
		
		while(cols.size() < maxSize){
			cols.add(new ArrayList<Long>());
		}
		
		
		for(List<Long> list: runs){
			for(int i = 0; i < list.size(); i++){
				Long l = list.get(i);
				cols.get(i).add(l);
			}
		}
		
		List<Double> averages =  new ArrayList<>();
		

		for(List<Long> col: cols){
			int sum = 0;
			int count = 0;
			for(Long l: col){
				sum+=l;
				count++;
			}
			
			
			
			averages.add((double) sum / (double) count);
			
		}
		
		
		return averages;
	}
}
