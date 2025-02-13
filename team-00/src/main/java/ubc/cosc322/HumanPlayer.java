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
        gameClient.sendMoveMessage(arrayToArrayList(moveInt[0]), arrayToArrayList(moveInt[1]), arrayToArrayList(moveInt[2]));
    }

    boolean checkStringSyntax (String moveString) { //L##L##L##
        if (moveString.length() > 9) {
            return false;
        } else {
            for (int i = 0; i<moveString.length(); i++) {
                System.out.println(moveString.charAt(i) + " " + i);
                
                if(i == 0){
                    if(moveString.charAt(i) >= 'a' && moveString.charAt(i) <= i){
                        System.out.println("Valid Move");
                        return true;
                    } else{
                        System.out.println("invalid move");
                        return false;
                    }
                } else{
                    if(moveString.charAt(i) >= 'a' && moveString.charAt(i) <= i){
                        System.out.println("Valid Move");
                        return true;
                    } else if(moveString.charAt(i) >= 0 || moveString.charAt(i) == 1){
                        if(moveString.charAt(i+1) == 0){
                            System.out.println("Valid Move at 10");
                            return true;
                        } else if(moveString.charAt(i+1) >= 0){
                            System.out.println("Invalid Move");
                            return false;
                        }else{
                        System.out.println("Valid Move");
                        return true;
                        }
                    }

                }
            }
        }

        return true;
    }

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