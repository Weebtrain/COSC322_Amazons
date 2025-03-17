package ubc.cosc322;

import java.util.ArrayList;

public class Extras {
    public static ArrayList<Integer> arrayToArrayList (int[] array) {
        ArrayList<Integer> returnList = new ArrayList<>(2);
        returnList.add(0, array[0]);
        returnList.add(1, array[1]);
        return returnList;
    }
}