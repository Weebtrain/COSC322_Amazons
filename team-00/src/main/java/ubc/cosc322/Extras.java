package ubc.cosc322;

import java.util.ArrayList;

public class Extras {
    public static ArrayList<Integer> arrayToArrayList (byte[] array) {
        ArrayList<Integer> returnList = new ArrayList<>(2);
        returnList.add(0, (int)array[0]);
        returnList.add(1, (int)array[1]);
        return returnList;
    }
}