
# Compilation Instructions
From the directory that contains this file, execute the command:

    `javac -d bin -cp bin /src/connect4/Main.java`

This will have compiled all classes and put them in /bin.

NB: The test package is left in the source code for your reference, but it
    will not be compiled.

# Execution Instructions

To run:

    `java -cp bin connect4.Main <Strategy> <Parameter> [first]`

Where:
* <Strategy> is the name of the strategy, supported strategies are:
  * RandomPlay
  * AntagonisticRandomPlay
  * BruteForce
  * Minimax [this strategy requires a search depth parameter]
  * MonteCarloTreeSearch [this strategy requires a search duration parameter]
* <Parameter> is the parameter to pass to the strategy. <Parameter> is
  only required for Minimax and MonteCarloTreeSearch Strategies
* first, if included, will make you first player. If first is ommited 
  then you will be the second player

# Execution Examples

To play against the computer that uses the RandomPlay Strategy:
    `java -cp bin connect4.Main RandomPlay`

If you want to go first then:
    `java -cp bin connect4.Main RandomPlay first`


To play against the computer that uses the Minimax Strategy with a search 
depth of 4:
    `java -cp bin connect4.Main Minimax 4`

To play against the computer that uses the MonteCarloTreeSearch Strategy 
with a search duration of 2 seconds of 4:
    `java -cp bin connect4.Main Minimax 4`