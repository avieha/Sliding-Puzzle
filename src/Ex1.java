import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.util.Collections.reverse;

public class Ex1 {

    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("input3.txt");
        Scanner sc = new Scanner(f);
        int index = 0;
        String Algo_type = ""; // stores the algorithm type
        int N = 0, M = 0;
        int empty_blocks;
        boolean time = false;
        boolean list_show = false;
        boolean goal_line = false;
        String[] size_param;
        String initial_string = "";
        String final_string = "";
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (index++ < 4) {
                switch (index) {
                    case 1:
                        Algo_type = line;
                        break;
                    case 2:
                        if (line.equals("with time"))
                            time = true;
                        break;
                    case 3:
                        if (line.equals("with open"))
                            list_show = true;
                        break;
                    case 4:
                        size_param = line.split("x");
                        N = Integer.parseInt(size_param[0]);
                        M = Integer.parseInt(size_param[1]);
                }
            } else if (line.equals("Goal state:")) {
                goal_line = true;
            } else if (!goal_line) {
                initial_string += line + ",";
            } else {
                final_string += line + ",";
            }
        }
        State start = state_create(initial_string.split(","), N, M);
        State goal = state_create(final_string.split(","), N, M);
        Algo alg = new Algo(start, goal, list_show);
        /*** WRITE TO A FILE ***/
        try {
            FileWriter fw = new FileWriter("output.txt");
            long begin = 0;
            State result = new State();
            long elapsedTime = 0;
            switch (Algo_type) {
                case "BFS":
                    begin = System.nanoTime();
                    result = alg.BFS(start, goal);
                    elapsedTime = System.nanoTime() - begin;
                    break;
                case "DFID":
                    begin = System.nanoTime();
                    result = alg.DFID(start, goal);
                    elapsedTime = System.nanoTime() - begin;
                    break;
                case "A*":
                    begin = System.nanoTime();
                    result = alg.Astar(start, goal);
                    elapsedTime = System.nanoTime() - begin;
                    break;
                case "IDA*":
                    begin = System.nanoTime();
                    result = alg.IDAstar(start, goal);
                    elapsedTime = System.nanoTime() - begin;
                    break;
                case "DFBnB":
                    begin = System.nanoTime();
                    result = alg.DFBnB(start, goal);
                    elapsedTime = System.nanoTime() - begin;
                    break;
            }
            if (result == null)
                fw.write("no path" + "\n");
            else fw.write(getPath(result) + "\n");
            fw.write("Num: " + result.getProduced() + "\n");
            fw.write("Cost: " + result.getCost() + "\n");
            if (time)
                fw.write((elapsedTime / Math.pow(10, 9)) + " seconds");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static State state_create(String[] str_state, int n, int m) {
        String[] index = new String[2];
        State s = new State();
        String[][] board = new String[n][m];
        int array_counter = 0;
        int blocks_count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (str_state[array_counter].equals("_")) {
                    index[blocks_count++] = (i + "," + j);
                }
                board[i][j] = str_state[array_counter];
                array_counter++;
            }
        }
        s.setIndex(index);
        s.setPuzzle(board);
        return s;
    }

    private static String getPath(State s) {
        List<String> list = new LinkedList<>();
        String result = "";
        while (s.getMove() != "") {
            list.add(s.getMove());
            s = s.getFather();
        }
        reverse(list);
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
            if (i != list.size() - 1)
                result += "-";
        }
        return result;
    }
}