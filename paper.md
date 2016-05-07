
_This document is intended to serve as a draft of the final paper. 
We can collaborate using this text file and then finally copy-paste it 
into our favourite word processor to get the journal format she requires._


# Intelligent Strategies for Playing Connection Games with Large Search Space

## Authors

Casey Beaird
Virginia Commonwealth University
School of Engineering Dept Computer Science
Richmond, Virginia
beairdcm@vcu.edu

Chris Everitt
Virginia Commonwealth University
School of Engineering Dept Computer Science
Richmond, Virginia
everittcj@vcu.edu

## Abstract

Connect four is a member of a
collection of games known as connection games.
Connection games are played by players who
place game pieces in turn with the goal of
creating a series of connected game pieces of a
desired length. We know from Nash[1] that for
any two player finite perfect information game
there exists a pure strategy. The accepted
method for analyzing dynamic games such as
connect for is by creating a game tree and
working from the bottom of the tree up to
evaluate the value of playing any particular
move. In theory this allows for any dynamic
perfect information game to be solved.
However, in practice even with significant
modern computing power many games remain
unsolved due to the immense size of their game
tree. We present a survey of strategies and their
effectiveness as played for the game connect
four. We implement and play a generic
connection game which allows us the direct
ability to manipulate the size of the game tree
and therefor the effectiveness of the different
strategies with respect to the size of the game.

## Introduction

Connect Four is a game that was
popularized by the Milton Bradly company in
XXXX. The origin of the game is not truly know
but many have claimed to have invented it.
Connect four has many names Captains Mistress,
â€¦., to name just a few. The game was
independently solved by two people. The first
XXX who solved the game in xxx. The second
solved the game in xxx. Xxx solved the game
using a xxx while xxx took a different approach
and solved the game using an eight ply database.

The game in it's traditional form is played
on a six by seven board with the winning
connection length being four. The number of
possible legal game states is stated as ### the
number is held in the list of numbers as ###. This
should provide some insight into the complexity of
solving the game using the traditional tree search.
Expanding the game board by even a single row or
column expands the search space by orders of
magnitude. It is this reason that heuristic playing
strategies are required to play intelligently.

The two main strategies we employ to deal
with the size of the game tree are first the minimax
strategy. We use a parameter to decide how deep
into the game tree to explore using this strategy.
Second is the Monte Carlo Tree Search (MCTS)
which expands the game tree where possible and
then plays a random game from that point. MCTS
uses a play clock parameter to limit the amount to
time the strategy is allowed determine it's next
move. We test these strategies against each other
and against several others to show how each
strategy fairs against the others. We also change
the size of the game board to see the impact this
has on both the effectiveness of the strategy as
well as how it changes the computational time
required to determine the next move to play.

The structure of this paper is laid out as
follows. Section 1 contains a brief overview of the
game of connect four as well as our goals. Section
2 covers in depth the strategies we used to conduct
our experiments, the details of how the strategy
works as well as the advantages and disadvantages
associated with each given strategy. Section 3
covers the experiments we used to asses our
hypothesis, and the reasoning behind it. Section 4
covers the results of our experiments, and finally
section 5 holds our conclusions.

## Background

The standard commercial version of Connect 4 is a game in which 2 adversarial players attempt to make a continuous run of four of their game pieces on a 7 by 6 cell board. The board itself is vertically oriented so that players may only place their pieces on the lowermost available cells. Game pieces, after being placed, cannot be removed for the rest of the game. The game ends when a player makes has place four of her pieces in a continuous row, column, or diagonal of the board; in this case that player has won. If the board is completely filled and the no player has won, then the game ends in a draw.

![A connect 4 game in progress](https://github.com/cBeaird/ConnectionGame/blob/master/connect4_inprogress.png)

_An abstract example of a Connect4 game in play, white and black cells are cells that are already occupied. The cells marked with circles are the current player’s possible moves. Black is the current player, and can win the game by placing a piece in the cell marked by the black circle, thereby completing a continuous vertical column of black pieces in the middle of the board._

## Strategies

### Random Play, Antagonistic Random Play, and Brute Force Strategies

A player’s strategy for our connect 4 game is responsible for choosing 
the next move from the set of legal moves that the player should make 
given the current game state. In addition to the Minimax and Monte Carlo strategies, we have implemented a number of other strategies to use as 
benchmarks.

The first strategy, Random, chooses a move from the legal 
move set at random every turn. The random strategy does not consider the 
game board at all, which means that this strategy will make to effort no 
block an opponent’s progress or win for itself. We think that the Random 
strategy is a good baseline against which to evaluate other strategies.

Our second strategy, called Antagonistic Random, builds on top of Random 
strategy with the addition of evaluating the board while choosing a 
move. Antagonistic Random will choose one of the winning moves if the 
current set of legal moves contains one or more moves that would win the 
game. If there is no way for Antagonistic Random to win on this turn, it 
will prevent it’s opponent from completing a winning run if it’s 
opponent can win on their next turn. If neither Antagonistic Random nor 
its opponent can win in this round, then it will choose a move at random 
just like Random would. We liken Antagonistic Random to a typical casual 
human player of the connect 4 game.

The Brute Force strategy is the first of our strategies that inspect the extensive form game tree for an optimal solution. Brute Force inspects the tree down to the game’s end states and assigns the utility of a cumulative utility of the node’s descendants, and ultimately to the end game leaf nodes where winning, losing, and drawing at the game’s end return high, low, and neutral utility for the player. 

### Minimax

In a minimax playing strategy, a player will attempt to maximize her utility under the assumption that her opponents will attempt to minimize it. For each of our turns, we will attempt to make a move that maximizes our utility. We assume that our opponent will choose a move that will minimize our utility. We implement the minimax algorithm by recursively depth-first-traversing a tree of node that represent game states. For any node, n, the current player of that node will be either our player or the opponent. A descendant of n will represent the update game state from the game state in n if the player at node n had made a move, m, out of the set of legal moves available at node n. In other words, node n will have a descendant node for each possible move at node n, and each descendant will represent the game state is if the player had made that move.

![An example of a game tree](https://github.com/cBeaird/ConnectionGame/blob/master/gametree.png)

_Above is an example of a minimax tree where each game state yields two possible moves. There are 4 plies below the root node. The plies alternate between our (circle) player and our opponent’s (square) player. The triangles represent final game states, where no more moves can be made. Triangle will be either a win, lose, or draw for our player._

In theory the entire tree could be searched and the leaf nodes would be the ending game states. We could then assign a utility for these end states and perfectly devaluate the utility of a move in any point of the game. As noted above though, the size of the search space for a standard connect 4 game is of the order of 10^12 makes such an approach infeasible. Instead we search only to a specific depth and evaluate the value of the node with a heuristic.

When the tree search reaches the maximum depth it can search, it first checks to see if the game is over, if the game is over, then the utility of that node is a function of the outcome of the game, with win utilities being greater than draws which are greater than loses. If the game is not yet over, then our heuristic evaluates the board to determine the utility. Our heuristic function at this point counts runs on the board for each player. A run is defined to be a consecutive line of pieces of the same color greater than one. The reasoning behind this heuristic is that if a player has many runs, then that player has many ways to win the game. Utility of the node is calculated by subtracted number of runs the opponent player has from the number of runs our player has.

### Monte Carlo Tree Search

## Experimentaion

## Results

Our two random strategies fared unsurprisingly the poorest against the more sophisticated strategies. When these two strategies lost, they lost quickly (after about 12 moves), and their wins game only after a game of many moves. The many moves required for the random strategies to win is consistent with our expectations since it would take many serendipitous moves to keep these strategies in the game until they had enough partial connections to have a reasonable change to randomly complete a partial connection.

With small boards, strategies were more likely to force a draw, and we believe this is for 2 reasons. First, since the winning connection length is fixed, larger boards offer more winning board configurations than smaller boards and so must necessarily end in draws. Second, the game tree searching strategies can see closer to the end of the game with smaller boards, allowing them to turn a potential loss into a draw instead.

Minimax Tree Search was not able to win more games than it lost against the entire field and in general performed more poorly as the game board increased in size. The decrease in performance can be explained by the increase in the solution space of the game, but perhaps a better heuristic would improve the performance. Notice also that the minimax strategy could not be exercised for the two largest boards at a depth of 6, the increase in branching factor caused this strategy to be intractable at larger board sizes.

Monte Carlo Tree Search has best win rate of all the strategies and usually tends to win more games than it loses. As the search space (the board sizes) increases, Monte Carlo Tree Search degrades but not a sharply as the Minimax strategy. Another notable result is that the Monte Carlo Tree Search strategy wins games in the fewest number of moves compared to the others. This may be considered evidence that this strategy is more making the best moves more consistently than other strategies.

## Conclusions

In this paper we explored the problem of the connection game Connect 4 and implemented a selection of computer algorithms to use as strategies for playing the game. We created a baseline random strategies and implemented the Minimax Tree Search and the Monte Carlo Tree Search algorithms. We exercised all of these strategies against the other strategies and found that Monte Carlo Tree Search was most successful at winning and degraded in quality lease as the boards sizes increased. For future work, we might try experimenting with different heuristics for the Minimax Tree Search; and also experiment with different winning connection lengths.

## References

[1] Nash, John F. "Equilibrium points in n-person games." Proc. Nat. Acad. Sci. USA 36.1 (1950): 48-49.

_Below is the big paper we discovered early int his project, 
we may choose to cite it so I am leaving it here_

[*] Allis, Victor. "A knowledge-based approach to connect-four. The game is solved: White wins." Masterâ€™s thesis, Vrije Universiteit. (1988)
