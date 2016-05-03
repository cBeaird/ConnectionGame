
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

## Strategies

### Random Play, Antagonistic Random Play, and Brute Force Strategies

A player’s strategy for our connect 4 game is responsible for choosing 
the next move from the set of legal moves that the player should make 
given the current game state. In addition to the Minimax and Monte Carlo strategies, we have implemented a number of other strategies to use as 
benchmarks. The first strategy, Random, chooses a move from the legal 
move set at random every turn. The random strategy does not consider the 
game board at all, which means that this strategy will make to effort to 
block an opponent’s progress or win for itself. We think that Random 
strategy is a good baseline against which to evaluate other players. Our 
second strategy, called Antagonistic Random, builds on top of Random 
strategy with the addition of evaluating the board while choosing a 
move. Antagonistic Random will choose one of the winning moves it the 
current set of legal moves contains one or more moves that would win the 
game. If there is no way for Antagonistic Random to win on this turn, it 
will prevent it’s opponent from completing a winning run if it’s 
opponent can win on their next turn. If neither Antagonistic Random nor 
its opponent can win in this round, then it will choose a move at random 
just like Random would. We liken Antagonistic Random to a typical casual 
human player of the connect 4 game. The Brute Force strategy is the 
first of our strategies that inspect the extensive form game tree for an 
optimal solution. Brute Force inspects the tree down to the game’s end 
states and assigns the utility of a cumulative utility of the node’s 
descendants, and ultimately to the end game leaf nodes where winning, 
losing, and drawing at the game’s end return high, low, and neutral 
utility for the player. 

### Minimax

### Monte Carlo Tree Search

## Experimentaion

## Results

## Conclusions

## References

[1] Nash, John F. "Equilibrium points in n-person games." Proc. Nat. Acad. Sci. USA 36.1 (1950): 48-49.

_Below is the big paper we discovered early int his project, 
we may choose to cite it so I am leaving it here_

[*] Allis, Victor. "A knowledge-based approach to connect-four. The game is solved: White wins." Masterâ€™s thesis, Vrije Universiteit. (1988)
