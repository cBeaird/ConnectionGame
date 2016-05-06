package connect4;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import connectionAPI.Game;
import connectionAPI.Player;
import connectionAPI.Strategy;

public class Main {
	private static Map<String, Class<? extends Strategy>> strategies;
	private static Map<String, String> parameters;
	
	private Class<Strategy> strategyClass = null;
	private int parameter = -1;
	private boolean first = false;
	
	static {
		strategies = new LinkedHashMap<String, Class<? extends Strategy>>();
		strategies.put("RandomPlay", RandomStrategy.class);
		strategies.put("AntagonisticRandomPlay", SmarterRandomStrategy.class);
		strategies.put("BruteForce", BruteForceStrategy.class);
		strategies.put("Minimax", TreeSearchStrategy.class);
		strategies.put("MonteCarloTreeSearch", MCTS_UCTStrategy.class);
		
		parameters = new HashMap<String, String>();
		parameters.put("Minimax", "search depth as an integer (eg 4)");
		parameters.put("MonteCarloTreeSearch", "maximum search duration in milliseconds (eg 3000)");
		
	}
	
	private void parseArgs(String[] args){
		if(args.length < 1 || args.length > 3){
			printUsage();
			System.exit(0);
		}
		
		String strategy = args[0];
		
		if(!strategies.containsKey(strategy)){
			System.out.println("ERROR: " + strategy + " is not a valid strategy");
			printUsage();
			System.exit(0);
		}
		
		// These come from the type-safe map of Strategies so we are safe to cast unchecked
		@SuppressWarnings("unchecked")
		Class<Strategy> strategyClass = (Class<Strategy>) strategies.get(strategy);
		this.strategyClass = strategyClass;

		
		if(parameters.containsKey(strategy)){
			if(args.length < 2){
				System.out.println("ERROR: For " + strategy + " you must provide the Parameter" + parameters.get(strategy));
				printUsage();
				System.exit(0);
			}
			try {
				this.parameter = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("ERROR: The parameter you passed ("+args[1]+") is not an integer");
				printUsage();
				System.exit(0);
			}
			
			if(args.length == 3){
				first = true;
			}
		} else {
			if(args.length == 2){
				first = true;
			}
		}
	}
	
	private Game setupGame(){
		
		Connect4Game c4g = null;
		ArrayList<Player> players = new ArrayList<>();
		
		Strategy human = new InteractiveStrategy();
		
		
		Strategy computerStrategy = null;
		try {
			Constructor<Strategy> cons;
			if(parameter!=-1){
				cons = strategyClass.getConstructor(Integer.TYPE);
				computerStrategy = cons.newInstance(parameter);
			} else {
				cons = strategyClass.getConstructor();
				computerStrategy = cons.newInstance();
			}
			
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		

		for (Player p : GamePieces.values()) {
			if (p == GamePieces.EMPTY)
				continue;
			if (p.turn() == 1) {
				if(first){
					p.setPlayerStrategy(computerStrategy);
				} else {
					p.setPlayerStrategy(human);
				}
			}
				
			if (p.turn() == 0){
				if(first){
					p.setPlayerStrategy(human);
				} else {
					p.setPlayerStrategy(computerStrategy);
				}
				
			}
				
			players.add(p);
		}
		
		c4g = new Connect4Game(6, 7, players);

		((Connect4Board) c4g.getGameBoard()).setConnectionLength(4);
		
		return c4g;
	}
	
	private void printUsage(){
		System.out.println("USAGE: connect4.Main <Strategy> <Parameter> [first]");
		System.out.println("  where <Strategy> is the Strategy the computer will use");
		System.out.println("  and <Parameter> is a parameter for the Strategy");
		System.out.println("  and omitting [first] will make you the second player");
		System.out.println("STRATEGIES:");
		for(String key: strategies.keySet()){
			System.out.print("  "+key);
			if(parameters.containsKey(key)){
				System.out.println(" Parameter: " + parameters.get(key));
			} else {
				System.out.println(" [no Parameter]");
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			Main m = new Main();
			m.parseArgs(args);
			Game game = m.setupGame();
			game.play();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
}
