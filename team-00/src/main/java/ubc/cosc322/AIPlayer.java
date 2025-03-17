package ubc.cosc322;

import java.util.PriorityQueue;

public class AIPlayer implements Runnable {
    private COSC322Test gameHandler = null;
    private byte[][] gameBoard = null;
    private int queenIdentity;
    private PriorityQueue<byte[][]> YoungOnes;

    private final int initialNodeCapacity = 1000000;

    public AIPlayer (COSC322Test handler, byte[][] curBoard, int queenId) {
        this.gameHandler = handler;
        this.gameBoard = curBoard;
        this.queenIdentity = queenId;
        this.YoungOnes = new PriorityQueue<byte[][]>(initialNodeCapacity);
    }

    @Override
    public void run () {

    }
}

/*
A*
Tree search
Game states, actions with game state
policy cost function
heuristic function -> calculate a heuristic based on game state -> killer heuristic
alpha-beta pruning AnakinSkywalker
priority queue of nodes
Kill based on time
Expand game states

find all possible moves for expanding
properly store large amounts of game states -> changed board state variable type to reduce memory consumption -> byte
priority queue might get too big?
*/