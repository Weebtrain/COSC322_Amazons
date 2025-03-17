package ubc.cosc322;

import java.util.Scanner;

public class HumanPlayer implements Runnable{
    private COSC322Test gameHandler = null;
    private byte[][] gameBoard = null;
    private int queenIdentity;

    HumanPlayer (COSC322Test handler, byte[][] curBoard, int queenId) {
        this.gameHandler = handler;

        this.gameBoard = curBoard;
        this.queenIdentity = queenId;
    }

    @Override
    public void run() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String move;
        byte[][] moveInt = null;
        do {
            System.out.print("\nEnter move: ");
            move = myObj.nextLine();  // Read user input
            move = move.toLowerCase();
            moveInt = checkMove(move);  //Check move returns null if move isn't valid for syntax reasons or if the move itself isn't valid
        } while (moveInt == null);
        gameHandler.updateGameState(moveInt, queenIdentity);

        gameHandler.SendGameMessage(moveInt);
    }

    boolean checkStringSyntax (String moveString) { //L##L##L##
        //converting move string to array
        char[] moveCharArray = moveString.toCharArray();

        if (moveCharArray.length > 9 || moveCharArray.length < 6) {
            return false;
        }

        for (int i = 0; i < moveCharArray.length; i++) {    //Iterates through character array

            if (i == 0) { // checks first character is a valid letter
                if (!(moveCharArray[i] >= 'a' && moveCharArray[i] <= 'j')) {
                    return false;
                }
            } else {
                // checks if last character was a letter, if so next character must be a number
                if (moveCharArray[i-1] >= 'a' && moveCharArray[i] <= 'j') {
                    if (!(moveCharArray[i] >= '0' && moveCharArray[i] <= '9')) {
                        return false;
                    }
                }
                // otherwise last character was a number, therefore next character must be a letter unless last character was a one, then it can be a zero
                else {
                    if (!((moveCharArray[i] >= 'a' && moveCharArray[i] <= 'j') || (moveCharArray[i-1] == '1' && moveCharArray[i] == '0'))) {
                        return false;
                    }
                }
            } 
        }
        return true;
    }


    byte[][] convertToMove (String moveString) {
        char[] moveCharArray = moveString.toCharArray();
        byte[][] arrayOfMoveStatements = new byte[3][2];
        String locations;
        int start = 0, end = 1;
        for (int i = 0; i<3;i++) {
            while (end<moveCharArray.length && !(moveCharArray[end] >= 'a' && moveCharArray[end] <= 'j')) { 
                end++;
            }
            locations = moveString.substring(start, end);
            arrayOfMoveStatements[i][1] = (byte)((int)(locations.charAt(0) - 'a') + 1);
            arrayOfMoveStatements[i][0] = (byte)Integer.parseInt(locations.substring(1));
            start = end;
            end++;
        }
        return arrayOfMoveStatements;
    }

    byte[][] checkMove (String moveString) {
        if (!(checkStringSyntax(moveString))) { //checks if string syntax is correct
            return null;
        }
        byte[][] tempMoves = convertToMove(moveString);  //converts string into integer arrays of x and y positions
        if (!(checkMoveValidity(tempMoves))) {  //checks if moves are valid (queen selection, queen move and arrow fire)
            return null;
        }
        return tempMoves;
    }

    boolean checkMoveValidity (byte[][] moves) {
        if(!ensureQueenPosition(moves[0])) return false;    //Checks your queen is selected
        if(!ensureValidDirection(moves[0], moves[1])) return false; //checks the queen can move there
        if(!ensureValidDirection(moves[1], moves[2])) return false; //checks the queen can shoot from move
        return true;
    }

    boolean ensureValidDirection (byte[] start, byte[] end) {
        if (start.equals(end) || gameBoard[end[0]-1][end[1]-1] == 3) {
            return false;
        } else if (start[0] == end[0]) {
            int direction = (start[1]-end[1])/Math.abs(start[1]-end[1]);
            for (int i = start[1]-direction; i != end[1]; i -= direction) {
                if (gameBoard[start[0]-1][i-1] != 0) {
                    return false;
                }
            }
            return true;
        } else if (start[1] == end[1]) {
            int direction = (start[0]-end[0])/Math.abs(start[0]-end[0]);
            for (int i = start[0]-direction; i != end[0]; i -= direction) {
                if (gameBoard[i-1][start[1]-1] != 0) {
                    return false;
                }
            }
            return true;
        } else if (Math.abs(start[0] - end[0]) == Math.abs(start[1] - end[1])) {
            //Checks if directions are positive or negative to look for barricades
            int xDirection = (start[0]-end[0])/Math.abs(start[0]-end[0]), yDirection = (start[1]-end[1])/Math.abs(start[1]-end[1]);
            int distance = start[0] - end[0];
            for (int i = 1; i<distance; i++) {
                if (gameBoard[start[0] + i*xDirection - 1][start[1] + i*yDirection - 1] != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    boolean ensureQueenPosition (byte[] position) {
        return gameBoard[position[0] - 1][position[1] - 1] == queenIdentity;
    }
}