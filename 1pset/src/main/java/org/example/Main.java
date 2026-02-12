package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static ArrayList<Integer> max_occur_ints(int[] arr) {
        HashSet<Integer> arr_set = new HashSet<Integer>();
        for (int n : arr) {
            arr_set.add(n);
        }
        int UNIQ_SIZE = arr_set.size();

        Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
        for (int n : arr) {
            int cur_val = counter.getOrDefault(n, 0);
            counter.put(n, cur_val+1);
        }
        ArrayList<ArrayList<Integer>> occurances = new ArrayList<ArrayList<Integer>>();
        for (int i=0; i<UNIQ_SIZE; i++) {
            occurances.add(new ArrayList<Integer>());
        }

        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            Integer num = entry.getKey();
            Integer times_seen = entry.getValue();
            occurances.get(times_seen).add(num);
        }

        for (ArrayList<Integer> result : occurances) {
            if (!result.isEmpty()) {
                return result; // result has all the integers that have occured the most
            }
        }
        return new ArrayList<Integer>();
    }
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        IO.println(String.format("Hello and welcome!"));

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            IO.println("i = " + i);
        }


        int[] test_inp = new int[] {5, 5, 3, 2, 1, -5, 12, 612, 6, 7, 7};
        ArrayList<Integer> res = max_occur_ints(test_inp);
        System.out.println(res);
    }
}
