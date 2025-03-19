package ubc.cosc322;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AIPlayer implements Runnable {
    private COSC322Test gameHandler = null;
    private gameState gameBoard = null;
    private int queenIdentity;
    private Policy p;
    private int maxDepth;

    public AIPlayer (COSC322Test handler, byte[][] curBoard, int queenId, float g, float w, float l, int maxD) {
        this.gameHandler = handler;
        this.gameBoard = new gameState(curBoard);
        this.queenIdentity = queenId;
        this.p = new Policy(g,w,l);
        this.maxDepth = maxD;
    }

    public void setGameState (byte[][] s) {
        gameBoard = new gameState(s);
    }

    @Override
    public void run () {
        startMaxValue(gameBoard, 0, 0, 0);
    }

    void startMaxValue(gameState s, float a, float b, int depth) {
        float v = -10000000;
        gameState currentBestMove = null;
        PriorityQueue<gameState> YoungOnes = generateStates(s, queenIdentity);
        while (!YoungOnes.isEmpty()) {
            gameState youngOne = YoungOnes.poll();
            float i = minValue(youngOne,a,b,depth+1);
            if (v < i) {
                v = i;
                currentBestMove = youngOne;
            }
            if (v >= b) {
                extractMoveAndSend(youngOne);
                return;   //killer move
            }
            a = Math.max(v,a);
        }
        extractMoveAndSend(currentBestMove);
    }

    void extractMoveAndSend (gameState move) {
        byte[][] moves = new byte[3][2];
        for (int i = 0; i<10; i++) {
			for (int j = 0; j<10; j++) {
				if (gameBoard.getBoardState()[i][j] == queenIdentity && move.getBoardState()[i][j] == 0) {
                    moves[0][0] = (byte)i;
                    moves[0][1] = (byte)j;
                }
                if (move.getBoardState()[i][j] == queenIdentity && gameBoard.getBoardState()[i][j] == 0) {
                    moves[1][0] = (byte)i;
                    moves[1][1] = (byte)j;
                }
                if (move.getBoardState()[i][j] == 3 && gameBoard.getBoardState()[i][j] == 0) {
                    moves[2][0] = (byte)i;
                    moves[2][1] = (byte)j;
                }
			}
		}
        gameHandler.updateGameState(moves, queenIdentity);

        gameHandler.SendGameMessage(moves);
    }

    float minValue(gameState s, float a, float b, int depth) {
        if (depth >= maxDepth) {
            return p.loss + p.general*depth;
        }
        float v = 10000000;
        PriorityQueue<gameState> YoungOnes = generateStates(s, 3-queenIdentity);
        if (YoungOnes.isEmpty()) return p.win + p.general*depth;
        while (!YoungOnes.isEmpty()) {
            gameState youngOne = YoungOnes.poll();
            v = Math.min(v,maxValue(youngOne,a,b,depth+1));
            if (v <= a) return v;
            b = Math.min(v,b);
        }
        return v;
    }

    float maxValue(gameState s, float a, float b, int depth) {
        if (depth >= maxDepth) {
            return p.loss + p.general*depth;
        }
        float v = -10000000;
        PriorityQueue<gameState> YoungOnes = generateStates(s, queenIdentity);
        if (YoungOnes.isEmpty()) return p.loss + p.general*depth;
        while (!YoungOnes.isEmpty()) {
            gameState youngOne = YoungOnes.poll();
            v = Math.max(v,minValue(youngOne,a,b,depth+1));
            if (v >= b) return v;   //killer move
            a = Math.max(v,a);
        }
        return v;
    }

    PriorityQueue<gameState> generateStates (gameState s, int queenId) {
        PriorityQueue<gameState> YoungOnes = new PriorityQueue<>();
        byte[][] queenPositions = Extras.FindQueens(s.getBoardState(), queenId);
        for (int i = 0; i<4; i++) {
            ArrayList<byte[][]> actionBoards = generateMoves(s.getBoardState(), queenPositions[i], queenId);
            while (!actionBoards.isEmpty()) { 
                byte[][] youngOne = actionBoards.removeFirst();
                YoungOnes.add(new gameState(youngOne, (short)0));   //need to generate heuristic
            }
        }
        return YoungOnes;
    }

    ArrayList<byte[][]> generateMoves (byte[][] board, byte[] queenPos, int queenId) {
        ArrayList<byte[][]> possibleBoards = new ArrayList<>();
        for (int i = queenPos[0]+1; i<10 && board[i][queenPos[1]] == 0; i++) {  //right direction
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[i][queenPos[1]] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {(byte)i,queenPos[1]}, queenId));
        }
        for (int i = queenPos[0]-1; i>=0 && board[i][queenPos[1]] == 0; i--) {  //left direction
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[i][queenPos[1]] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {(byte)i,queenPos[1]}, queenId));
        }
        for (int i = queenPos[1]+1; i<10 && board[queenPos[0]][i] == 0; i++) {  //up direction
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[queenPos[0]][i] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {queenPos[0],(byte)i}, queenId));
        }
        for (int i = queenPos[1]-1; i<10 && board[queenPos[0]][i] == 0; i--) {  //down direction
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[queenPos[0]][i] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {queenPos[0],(byte)i}, queenId));
        }
        for (int i = 1; queenPos[0] + i<10 && queenPos[1] + i<10 && board[queenPos[0]+i][queenPos[1]+i] == 0; i++) {    //right and up
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[queenPos[0]+i][queenPos[1]+i] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {(byte)(queenPos[0]+i),(byte)(queenPos[1]+i)}, queenId));
        }
        for (int i = 1; queenPos[0] + i<10 && queenPos[1] - i>=0 && board[queenPos[0]+i][queenPos[1]-i] == 0; i++) {    //right and down
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[queenPos[0]+i][queenPos[1]-i] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {(byte)(queenPos[0]+i),(byte)(queenPos[1]-i)}, queenId));
        }
        for (int i = 1; queenPos[0] - i>=0 && queenPos[1] + i<10 && board[queenPos[0]-i][queenPos[1]+i] == 0; i++) {    //left and up
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[queenPos[0]-i][queenPos[1]+i] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {(byte)(queenPos[0]-i),(byte)(queenPos[1]+i)}, queenId));
        }
        for (int i = 1; queenPos[0] - i>=0 && queenPos[1] - i>=0 && board[queenPos[0]-i][queenPos[1]-i] == 0; i++) {    //left and down
            byte[][] newBoard = board.clone();
            newBoard[queenPos[0]][queenPos[1]] = 0;
            newBoard[queenPos[0]-i][queenPos[1]-i] = (byte)queenId;
            possibleBoards.addAll(moveToArrowBoardCreation(newBoard, new byte[] {(byte)(queenPos[0]-i),(byte)(queenPos[1]-i)}, queenId));
        }
        return possibleBoards;
    }

    ArrayList<byte[][]> moveToArrowBoardCreation (byte[][] board, byte[] movePos, int queenId) {
        ArrayList<byte[][]> possibleBoards = new ArrayList<>();
        for (int i = movePos[0]+1; i<10 && board[i][movePos[1]] == 0; i++) {  //right direction
            byte[][] newBoard = board.clone();
            newBoard[i][movePos[1]] = 3;
            possibleBoards.add(newBoard);
        }
        for (int i = movePos[0]-1; i>=0 && board[i][movePos[1]] == 0; i--) {  //left direction
            byte[][] newBoard = board.clone();
            newBoard[i][movePos[1]] = 3;
            possibleBoards.add(newBoard);
        }
        for (int i = movePos[1]+1; i<10 && board[movePos[0]][i] == 0; i++) {  //up direction
            byte[][] newBoard = board.clone();
            newBoard[movePos[0]][i] = 3;
            possibleBoards.add(newBoard);
        }
        for (int i = movePos[1]-1; i<10 && board[movePos[0]][i] == 0; i--) {  //down direction
            byte[][] newBoard = board.clone();
            newBoard[movePos[0]][i] = 3;
            possibleBoards.add(newBoard);
        }
        for (int i = 1; movePos[0] + i<10 && movePos[1] + i<10 && board[movePos[0]+i][movePos[1]+i] == 0; i++) {    //right and up
            byte[][] newBoard = board.clone();
            newBoard[movePos[0]+i][movePos[1]+i] = 3;
            possibleBoards.add(newBoard);
        }
        for (int i = 1; movePos[0] + i<10 && movePos[1] - i>=0 && board[movePos[0]+i][movePos[1]-i] == 0; i++) {    //right and down
            byte[][] newBoard = board.clone();
            newBoard[movePos[0]+i][movePos[1]-i] = 3;
            possibleBoards.add(newBoard);
        }
        for (int i = 1; movePos[0] - i>=0 && movePos[1] + i<10 && board[movePos[0]-i][movePos[1]+i] == 0; i++) {    //left and up
            byte[][] newBoard = board.clone();
            newBoard[movePos[0]-i][movePos[1]+i] = 3;
            possibleBoards.add(newBoard);
        }
        for (int i = 1; movePos[0] - i>=0 && movePos[1] - i>=0 && board[movePos[0]-i][movePos[1]-i] == 0; i++) {    //left and down
            byte[][] newBoard = board.clone();
            newBoard[movePos[0]-i][movePos[1]-i] = 3;
            possibleBoards.add(newBoard);
        }
        return possibleBoards;
    }
}

class gameState implements Comparable<gameState> {
    byte[][] boardState;
    short h = 0;

    public gameState (byte[][] state) {
        this.boardState = state;
    }
    public gameState (byte[][] state, short heuristic) {
        this.boardState = state;
        this.h = heuristic;
    }

    public byte[][] getBoardState () {
        return boardState;
    }

    public short getH () {
        return h;
    }

    @Override
    public int compareTo (gameState g) {
        return this.h - g.h;
    }
}

class Policy {
    public float general;
    public float win;
    public float loss;

    Policy (float g, float w, float l) {
        general = g;
        win = w;
        loss = l;
    }
}

/*
Minimax alpha beta
Tree search
Game states, actions with game state
policy cost function
heuristic function -> calculate a heuristic based on game state -> killer heuristic
alpha-beta pruning AnakinSkywalker
queue of nodes
Kill based on time
Expand game states

find all possible moves for expanding nodes
properly store large amounts of game states -> changed board state variable type to reduce memory consumption -> byte
*/