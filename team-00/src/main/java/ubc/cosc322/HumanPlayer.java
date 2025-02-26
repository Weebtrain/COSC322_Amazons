package ubc.cosc322;

import java.util.ArrayList;
import java.util.Scanner;

import ygraph.ai.smartfox.games.GameClient;

public class HumanPlayer implements Runnable{
    //private COSC322Test gameHandler = null;
    private GameClient gameClient = null;
    private int[][] gameBoard = null;
    private int queenIdentity;

    HumanPlayer (GameClient client, int[][] curBoard, int queenId) {
        this.gameClient = client;
        this.gameBoard = curBoard;
        this.queenIdentity = queenId;
        System.out.print(queenIdentity);
    }

    @Override
    public void run() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String move;
        int[][] moveInt = null;
        do {
            System.out.print("\nEnter move: ");
            move = myObj.nextLine();  // Read user input
            move = move.toLowerCase();
            moveInt = checkMove(move);  //Check move returns null if move isn't valid for syntax reasons or if the move itself isn't valid
        } while (moveInt == null);
        System.out.println("Sending Move");
        gameClient.sendMoveMessage(arrayToArrayList(moveInt[0]), arrayToArrayList(moveInt[1]), arrayToArrayList(moveInt[2]));
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


    int[][] convertToMove (String moveString) {
        char[] moveCharArray = moveString.toCharArray();
        int[][] arrayOfMoveStatements = new int[3][2];
        String locations;
        int start = 0, end = 1;
        for (int i = 0; i<3;i++) {
            while (end<moveCharArray.length && !(moveCharArray[end] >= 'a' && moveCharArray[end] <= 'j')) { 
                end++;
            }
            locations = moveString.substring(start, end);
            arrayOfMoveStatements[i][1] = (int)(locations.charAt(0) - 'a') + 1;
            arrayOfMoveStatements[i][0] = Integer.parseInt(locations.substring(1));
            start = end;
            end++;
        }
        return arrayOfMoveStatements;
    }

    int[][] checkMove (String moveString) {
        if (!(checkStringSyntax(moveString))) { //checks if string syntax is correct
            return null;
        }
        System.out.println("Syntax passed");
        int[][] tempMoves = convertToMove(moveString);  //converts string into integer arrays of x and y positions
        System.out.println("Move conversion passed");
        if (!(checkMoveValidity(tempMoves))) {  //checks if moves are valid (queen selection, queen move and arrow fire)
            return null;
        }
        System.out.println("Move Valid passed");
        return tempMoves;
    }

    boolean checkMoveValidity (int[][] moves) {
        if(!ensureQueenPosition(moves[0])) return false;    //Checks your queen is selected
        System.out.println("Queen Position Valid");
        if(!ensureValidDirection(moves[0], moves[1])) return false; //checks the queen can move there
        System.out.println("Queen Move Valid");
        if(!ensureValidDirection(moves[1], moves[2])) return false; //checks the queen can shoot from move
        System.out.println("Queen Shot Valid");
        return true;
    }

    boolean ensureValidDirection (int[] start, int[] end) {
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

    boolean ensureQueenPosition (int[] position) {
        System.out.println("Queen of " + gameBoard[position[0] - 1][position[1] - 1] + " at " + position[0] + "," + position[1]);
        return gameBoard[position[0] - 1][position[1] - 1] == queenIdentity;
    }

    ArrayList<Integer> arrayToArrayList (int[] array) {
        ArrayList<Integer> returnList = new ArrayList<>(2);
        returnList.add(0, array[0]);
        returnList.add(1, array[1]);
        return returnList;
    }
}