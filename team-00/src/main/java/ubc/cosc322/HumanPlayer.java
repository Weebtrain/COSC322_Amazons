package ubc.cosc322;

import java.util.ArrayList;
import java.util.Scanner;

import ygraph.ai.smartfox.games.GameClient;

public class HumanPlayer implements Runnable{
    //private COSC322Test gameHandler = null;
    private GameClient gameClient = null;

    HumanPlayer (GameClient client) {
        this.gameClient = client;
    }

    public void run() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String move;
        do {
            System.out.print("Enter move: ");
            move = myObj.nextLine();  // Read user input
            //move = move.toLowerCase();
        } while (!checkStringSyntax(move));
        int[][] moveInt = convertToMove(move);
        //check move validity
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
                if (!(moveCharArray[i] >= 'a' && moveCharArray[i] <= 'i')) {
                    return false;
                }
            } else {
                // checks if last character was a letter, if so next character must be a number
                if (moveCharArray[i-1] >= 'a' && moveCharArray[i] <= 'i') {
                    if (!(moveCharArray[i] >= '0' && moveCharArray[i] <= '9')) {
                        return false;
                    }
                }
                // otherwise last character was a number, therefore next character must be a letter unless last character was a one, then it can be a zero
                else {
                    if (!((moveCharArray[i] >= 'a' && moveCharArray[i] <= 'i') || (moveCharArray[i-1] == '1' && moveCharArray[i] == '0'))) {
                        return false;
                    }
                }
            } 
        }
        return true;
    }

    //To be fixed
    int[][] convertToMove (String moveString) {
        int[][] arrayOfMoveStatements = new int[3][2];
        String[] locations = new String[3];
        int start = 0, end = 1;
        for (int i = 0; i<locations.length;i++) {
            while (!(moveString.charAt(end) >= 'a' && moveString.charAt(end) <= 'i')) { 
                end++;
            }
            locations[i] = moveString.substring(start, end);
            arrayOfMoveStatements[i][0] = (int)(locations[i].charAt(0) - 'a') + 1;
            arrayOfMoveStatements[i][1] = Integer.parseInt(locations[i].substring(1));
        }
        
        return arrayOfMoveStatements;
    }

    ArrayList<Integer> arrayToArrayList (int[] array) {
        ArrayList<Integer> returnList = new ArrayList<Integer>(2);
        returnList.add(0, array[0]);
        returnList.add(1, array[1]);
        return returnList;
    }
}