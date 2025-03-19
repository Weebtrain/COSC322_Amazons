package ubc.cosc322;

import java.util.ArrayList;

public class Extras {
    public static ArrayList<Integer> arrayToArrayList (byte[] array) {
        ArrayList<Integer> returnList = new ArrayList<>(2);
        returnList.add(0, (int)array[0]);
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
}