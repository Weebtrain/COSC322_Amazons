package ubc.cosc322;

import java.util.ArrayList;

public class Extras {
    public static ArrayList<Integer> arrayToArrayList (byte[] array) {
        ArrayList<Integer> returnList = new ArrayList<>(2);
        returnList.add(0, (int)(9-array[0]));
        returnList.add(1, (int)array[1]);
        return returnList;
    }

    public static byte[][] FindQueens (byte[][] board, int queenId) {
        byte[][] queenPositions = new byte[4][2];
        int queenCount = 0;
        for (int i = 0; i<10; i++) {
			for (int j = 0; j<10; j++) {
                if (queenCount >= 4) {
                    return queenPositions;
                }
				if (board[i][j] == queenId) {
                    queenPositions[queenCount][0] = (byte)i;
                    queenPositions[queenCount][1] = (byte)j;
                    queenCount++;
                }
			}
		}
        return queenPositions;
    }

    public static void displayGameStateArray(byte[][] board) {	//For debugging purposes only, commented out in updateGameState functions
		for (int i = 0; i<10; i++) {
			for (int j = 0; j<10; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
        System.out.println();
	}

    public static byte[][] cloneMatrix (byte[][] s) {
        byte[][] f = new byte[s.length][s[0].length];
        for (int i = 0; i<10; i++) {
			for (int j = 0; j<10; j++) {
				f[i][j] = s[i][j];
			}
		}
        return f;
    }
}