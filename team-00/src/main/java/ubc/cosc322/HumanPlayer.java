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
        //converting move string to array
        char[] Characters = moveString.toCharArray();

        if (moveString.length() > 9) {
            return false;
        } else {

            for (int i = 0; i < moveString.length(); i++) {
                // storing current char into an var we iterate through for checking
                char currentChar = Characters[i];

                System.out.println(Characters[i] + " " + i);
                System.out.println(currentChar);

                if (i == 0) {

                    if (!(currentChar >= 'a' && currentChar <= 'i')) {
                        System.out.println("invalid move, this is line 44");
                        return false;
                    }

                } else{

                    if (!(currentChar >= '0' && currentChar <= '9')) {
                            System.out.println("invalid move, this is line 51");
                            System.out.print("IT IS I " +  currentChar);
                            return false;
                        }

                    } 
                }
            }
                System.out.println("I have completed");
        return true;
    }



       /*if (moveString.length() > 9) {
            return false;
        } else {
            for (int i = 0; i< moveString.length(); i++) {
                System.out.println(Characters[i] + " " + i);
                
                if(i == 0){
                    if( Characters[i] >= 'a' &&  Characters[i] <= 'i'){
                        System.out.println("Valid Move, this is line 42");  
                    } else{
                        System.out.println("invalid move, this is line 44");
                        System.out.println("IT IS I " +  Characters[i]); 
                    }
                } else{
                    if( Characters[i] >= 'a' &&  Characters[i] <= 'i'){
                        System.out.println("Valid Move , this is line 49");
                    } else if( Characters[i] >= '0' ||  Characters[i] == '1'){
                        if( Characters[i-1] == '0'){
                            System.out.println("Valid Move, this is line 52");
                        } else if(Characters[i-1] >= '0'){
                            System.out.println("Invalid Move, this is line 54" + " "+  (int)Characters[i]);
                            if( Characters[i-1] ==  Characters[i-1]){
                                System.out.println("IT IS I " +  Characters[i]);
                            }
                        }else{
                        System.out.println("Valid Move, this is line 59");
                        } */


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