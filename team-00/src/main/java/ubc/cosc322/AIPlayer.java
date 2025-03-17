package ubc.cosc322;

public class AIPlayer implements Runnable {
    private COSC322Test gameHandler = null;
    private int[][] gameBoard = null;
    private int queenIdentity;

    public AIPlayer (COSC322Test handler, int[][] curBoard, int queenId) {
        this.gameHandler = handler;
        this.gameBoard = curBoard;
        this.queenIdentity = queenId;
    }

    @Override
    public void run () {

    }
}