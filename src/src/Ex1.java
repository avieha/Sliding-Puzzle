package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Collections.reverse;

public class Ex1 {

    static int N = 0, M = 0;
    static int empty_blocks;

    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("C:\\Users\\Aviem\\IdeaProjects\\Nxm-tile puzzle\\src\\src\\input2.txt");
        Scanner sc = new Scanner(f);
        int index = 0;
        String Algo_type = ""; // stores the algorithm type
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
        String[] initial_state = initial_string.split(",");
        String[] final_state = final_string.split(",");
        State start = state_create(initial_state);
        State goal = state_create(final_state);
        //System.out.println(Algo_type +" "+ time + " " + list_show + " " + N + " " + M+" "+ Arrays.deepToString(initial_state)+" "+Arrays.deepToString(final_state));
        long begin = System.nanoTime();
        State result = BFS(start, goal);
        long elapsedTime = System.nanoTime() - begin;
        getPath(result);
        System.out.println("Num: " + result.getProduced());
        System.out.println("Cost: " + result.getCost());
        System.out.println((elapsedTime / Math.pow(10, 9)) + " seconds");
    }

    private static State state_create(String[] str_state) {
        String[] index = new String[2];
        State s = new State();
        String[][] board = new String[N][M];
        int array_counter = 0;
        int blocks_count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (str_state[array_counter].equals("_")) {
                    index[blocks_count++] = (i + "," + j);
                }
                board[i][j] = str_state[array_counter];
                array_counter++;
            }
        }
        s.setIndex(index);
        s.setPuzzle(board);
        empty_blocks = blocks_count;
        return s;
    }

    public static State BFS(State start, State Goal) {
        HashSet<State> open_list = new HashSet<>();
        LinkedList<State> q = new LinkedList<>();
        int counter = 0;
        open_list.add(start);
        q.add(start);
        while (!q.isEmpty()) {
            State n = q.removeFirst();
            n.setVisited(true);
            List<State> list = children(n);
            counter += list.size();
            for (State s : list) {
                if (!open_list.contains(s) && !s.getVisited()) {
                    if (s.equals(Goal)) {
                        s.setProduced(counter);
                        return s;
                    }
                    open_list.add(s);
                    q.add(s);
                }
            }
        }
        return null;
    }

    public static void DFID(State state, State Goal) {

    }

    public static void Astar(State state, State Goal) {

    }

    public static void IDAstar(State state, State Goal) {

    }

    public static void DFBnB(State state, State Goal) {

    }

    private static void getPath(State s) {
        List<String> list = new LinkedList<>();
        while (s.getMove() != "") {
            list.add(s.getMove());
            s = s.getFather();
        }
        reverse(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i != list.size() - 1)
                System.out.print("-");
        }
        System.out.println();
    }

    private static List<State> children(State father) {
        List<State> child_list = new LinkedList<>();
        if (empty_blocks == 1) {
            child_list = one_block(father);
        } else if (empty_blocks == 2) {
            child_list = two_blocks(father);
        }
        return child_list;
    }

    private static List<State> one_block(State father) {
        String[] location;
        List<State> list = new LinkedList<>();
        location = (father.getIndex()[0]).split(","); // "23,4"
        int i = Integer.parseInt(location[0]);
        int j = Integer.parseInt(location[1]);
        if ((j + 1) < M) {
            String[][] first_temp_board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = first_temp_board[i][j];
            first_temp_board[i][j] = first_temp_board[i][j + 1];
            first_temp_board[i][j + 1] = temp;
            String number_moved = first_temp_board[i][j] + "L";
            State a = new State(first_temp_board, father.getCost() + 5, father, number_moved, new String[]{i + "," + (j + 1), null});
            if (!a.equals(father.getFather())) {
                list.add(a);
            }
        }
        if ((i + 1) < N) {
            String[][] second_temp_board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp2 = second_temp_board[i][j];
            second_temp_board[i][j] = second_temp_board[i + 1][j];
            second_temp_board[i + 1][j] = temp2;
            String number_moved2 = second_temp_board[i][j] + "U";
            State a = new State(second_temp_board, father.getCost() + 5, father, number_moved2, new String[]{(i + 1) + "," + j, null});
            if (!a.equals(father.getFather())) {
                list.add(a);
            }
        }
        if ((j - 1) >= 0) {
            String[][] third_temp_board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp3 = third_temp_board[i][j];
            third_temp_board[i][j] = third_temp_board[i][j - 1];
            third_temp_board[i][j - 1] = temp3;
            String number_moved3 = third_temp_board[i][j] + "R";
            State a = new State(third_temp_board, father.getCost() + 5, father, number_moved3, new String[]{i + "," + (j - 1), null});
            if (!a.equals(father.getFather())) {
                list.add(a);
            }
        }
        if ((i - 1) >= 0) {
            String[][] fourth_temp_board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp4 = fourth_temp_board[i][j];
            fourth_temp_board[i][j] = fourth_temp_board[i - 1][j];
            fourth_temp_board[i - 1][j] = temp4;
            String number_moved4 = fourth_temp_board[i][j] + "D";
            State a = new State(fourth_temp_board, father.getCost() + 5, father, number_moved4, new String[]{(i - 1) + "," + j, null});
            if (!a.equals(father.getFather())) {
                list.add(a);
            }
        }
        return list;
    }

    public static List<State> two_blocks(State father) {
        String[][] board;
        List<State> list = new LinkedList<>();
        String[] first = father.getIndex()[0].split(",");
        String[] second = father.getIndex()[1].split(",");
        int i1 = Integer.parseInt(first[0]); // always the closest to the
        int j1 = Integer.parseInt(first[1]); // top of the matrix
        int i2 = Integer.parseInt(second[0]);
        int j2 = Integer.parseInt(second[1]);
        boolean tight = false;
        boolean up=false;
        boolean side=false;
        if (check_distance(first, second) == 3)
            tight = true;
        if (tight) {
            // vertical twin
            if (i2 > i1) {
                up=true;
                if ((j1 + 1) < M && (j2 + 1) < M) {
                    board = puzzle_copy(father.getPuzzle());
                    String temp = board[i2][j2];
                    board[i2][j2] = board[i2][j2 + 1];
                    board[i2][j2 + 1] = temp;
                    board[i1][j1] = board[i1][j1 + 1];
                    board[i1][j1 + 1] = temp;
                    String number_moved = board[i1][j1] + "&" + board[i2][j2] + "L";
                    State a = new State(board, father.getCost() + 6, father, number_moved, new String[]{i1 + "," + (j1 + 1), i2 + "," + (j2 + 1)});
                    if (!a.equals(father.getFather()) && !a.equals(father))
                        list.add(a);
                }
                if ((j1 - 1) >= 0 && (j2 - 1) >= 0) {
                    board = puzzle_copy(father.getPuzzle());
                    String temp = board[i2][j2];
                    board[i2][j2] = board[i2][j2 - 1];
                    board[i2][j2 - 1] = temp;
                    board[i1][j1] = board[i1][j1 - 1];
                    board[i1][j1 - 1] = temp;
                    String number_moved = board[i1][j1] + "&" + board[i2][j2] + "R";
                    State a = new State(board, father.getCost() + 6, father, number_moved, new String[]{i1 + "," + (j1 - 1), i2 + "," + (j2 - 1)});
                    if (!a.equals(father.getFather()) && !a.equals(father))
                        list.add(a);
                }
            }
            else if (j2 > j1) { // horizontal twin
                side=true;
                if ((i1 + 1) < N && (i2 + 1) < N) {
                    board = puzzle_copy(father.getPuzzle());
                    // Swap
                    String temp = board[i1][j1];
                    board[i1][j1] = board[i1 + 1][j1];
                    board[i2][j2] = board[i2 + 1][j2];
                    board[i1 + 1][j1] = temp;
                    board[i2 + 1][j2] = temp;
                    String number_moved = board[i1][j1] + "&" + board[i2][j2] + "U";
                    State a = new State(board, father.getCost() + 7, father, number_moved, new String[]{(i1 + 1) + "," + j1, (i2 + 1) + "," + j2});
                    if (!a.equals(father.getFather()) && !a.equals(father))
                        list.add(a);
                }
                if ((i1 - 1) >= 0 && (i2 - 1) >= 0) {
                    board = puzzle_copy(father.getPuzzle());
                    // Swap
                    String temp = board[i1][j1];
                    board[i1][j1] = board[i1 - 1][j1];
                    board[i2][j2] = board[i2 - 1][j2];
                    board[i1 - 1][j1] = temp;
                    board[i2 - 1][j2] = temp;
                    String number_moved = board[i1][j1] + "&" + board[i2][j2] + "D";
                    State a = new State(board, father.getCost() + 7, father, number_moved, new String[]{(i1 - 1) + "," + j1, (i2 - 1) + "," + j2});
                    if (!a.equals(father.getFather()) && !a.equals(father))
                        list.add(a);
                }
            }
        }
        if ((j1 + 1) < M && !side) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i1][j1];
            board[i1][j1] = board[i1][j1 + 1];
            board[i1][j1 + 1] = temp;
            String number_moved = board[i1][j1] + "L";
            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + (j1 + 1), i2 + "," + j2});
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }
        if ((i1 + 1) < N && !up) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i1][j1];
            board[i1][j1] = board[i1 + 1][j1];
            board[i1 + 1][j1] = temp;
            String number_moved = board[i1][j1] + "U";
            String[] s;
            if (i1 + 1 >= i2)
                s = new String[]{i2 + "," + j2, (i1 + 1) + "," + j1};
            else s = new String[]{(i1 + 1) + "," + j1, i2 + "," + j2};
            State a = new State(board, father.getCost() + 5, father, number_moved, s);
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }
        if ((j1 - 1) >= 0) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i1][j1];
            board[i1][j1] = board[i1][j1 - 1];
            board[i1][j1 - 1] = temp;
            String number_moved = board[i1][j1] + "R";
            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + (j1 - 1), i2 + "," + j2});
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }
        if ((i1 - 1) >= 0) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i1][j1];
            board[i1][j1] = board[i1 - 1][j1];
            board[i1 - 1][j1] = temp;
            String number_moved = board[i1][j1] + "D";
            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{(i1 - 1) + "," + j1, i2 + "," + j2});
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }
        if ((j2 + 1) < M) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i2][j2];
            board[i2][j2] = board[i2][j2 + 1];
            board[i2][j2 + 1] = temp;
            String number_moved = board[i2][j2] + "L";
            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, i2 + "," + (j2 + 1)});
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }
        if ((i2 + 1) < N) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i2][j2];
            board[i2][j2] = board[i2 + 1][j2];
            board[i2 + 1][j2] = temp;
            String number_moved = board[i2][j2] + "U";
            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, (i2 + 1) + "," + j2});
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }
        if ((j2 - 1) >= 0 && !side) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i2][j2];
            board[i2][j2] = board[i2][j2 - 1];
            board[i2][j2 - 1] = temp;
            String number_moved = board[i2][j2] + "R";
            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, i2 + "," + (j2 - 1)});
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }
        if ((i2 - 1) >= 0 && !up) {
            board = puzzle_copy(father.getPuzzle());
            // Swap
            String temp = board[i2][j2];
            board[i2][j2] = board[i2 - 1][j2];
            board[i2 - 1][j2] = temp;
            String number_moved = board[i2][j2] + "D";
            String[] s;
            if (i2 - 1 <= i1 && j2 < j1)
                s = new String[]{(i2 - 1) + "," + j2, i1 + "," + j1};
            else s = new String[]{i1 + "," + j1, (i2 - 1) + "," + j2};
            State a = new State(board, father.getCost() + 5, father, number_moved, s);
            if (!a.equals(father.getFather()) && !a.equals(father))
                list.add(a);
        }

//        if ((i1 + 1) < N) {
//            board = puzzle_copy(father.getPuzzle());
//            // Swap
//            String temp = board[i1][j1];
//            board[i1][j1] = board[i1 + 1][j1];
//            board[i1 + 1][j1] = temp;
//            String number_moved = board[i1][j1] + "U";
//            String[] s;
//            if (i1 + 1 > i2)
//                s = new String[]{i2 + "," + j2, (i1 + 1) + "," + j1};
//            else s = new String[]{(i1 + 1) + "," + j1, i2 + "," + j2};
//            State a = new State(board, father.getCost() + 5, father, number_moved, s);
//            if (!a.equals(father.getFather()) && !a.equals(father))
//                list.add(a);
//        }
//        if ((j1 - 1) >= 0) {
//            board = puzzle_copy(father.getPuzzle());
//            // Swap
//            String temp = board[i1][j1];
//            board[i1][j1] = board[i1][j1 - 1];
//            board[i1][j1 - 1] = temp;
//            String number_moved = board[i1][j1] + "R";
//            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + (j1 - 1), i2 + "," + j2});
//            if (!a.equals(father.getFather()) && !a.equals(father))
//                list.add(a);
//        }
//        if ((i1 - 1) >= 0) {
//            board = puzzle_copy(father.getPuzzle());
//            // Swap
//            String temp = board[i1][j1];
//            board[i1][j1] = board[i1 - 1][j1];
//            board[i1 - 1][j1] = temp;
//            String number_moved = board[i1][j1] + "D";
//            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{(i1 - 1) + "," + j1, i2 + "," + j2});
//            if (!a.equals(father.getFather()) && !a.equals(father))
//                list.add(a);
//        }
//        if ((j2 + 1) < M) {
//            board = puzzle_copy(father.getPuzzle());
//            // Swap
//            String temp = board[i2][j2];
//            board[i2][j2] = board[i2][j2 + 1];
//            board[i2][j2 + 1] = temp;
//            String number_moved = board[i2][j2] + "L";
//            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, i2 + "," + (j2 + 1)});
//            if (!a.equals(father.getFather()) && !a.equals(father))
//                list.add(a);
//        }
//        if ((i2 + 1) < N) {
//            board = puzzle_copy(father.getPuzzle());
//            // Swap
//            String temp = board[i2][j2];
//            board[i2][j2] = board[i2 + 1][j2];
//            board[i2 + 1][j2] = temp;
//            String number_moved = board[i2][j2] + "U";
//            State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, (i2 + 1) + "," + j2});
//            if (!a.equals(father.getFather()) && !a.equals(father))
//                list.add(a);
//        }
//        if ((i2 - 1) >= 0) {
//            board = puzzle_copy(father.getPuzzle());
//            // Swap
//            String temp = board[i2][j2];
//            board[i2][j2] = board[i2 - 1][j2];
//            board[i2 - 1][j2] = temp;
//            String number_moved = board[i2][j2] + "D";
//            String[] s;
//            if (i2 - 1 < i1)
//                s = new String[]{(i2 - 1) + "," + j2, i1 + "," + j1};
//            else s = new String[]{i1 + "," + j1, (i2 - 1) + "," + j2};
//            State a = new State(board, father.getCost() + 5, father, number_moved, s);
//            if (!a.equals(father.getFather()) && !a.equals(father))
//                list.add(a);
//        }
        return list;
    }

    private static String[][] puzzle_copy(String[][] puzzle) {
        String[][] board = new String[N][M];
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                board[i][j] = puzzle[i][j];
            }
        }
        return board;
    }

    private static int check_distance(String[] first, String[] second) {
        int i1 = Integer.parseInt(first[0]); // 0
        int j1 = Integer.parseInt(first[1]); // 1
        int i2 = Integer.parseInt(second[0]); // 2
        int j2 = Integer.parseInt(second[1]); // 1
        int distance_between = (int) Math.sqrt(Math.pow(i2 - i1, 2) + Math.pow(j2 - j1, 2));
        int distance1 = i1 + j1;
        int distance2 = i2 + j2;
        if (distance_between == 1) {
            return 3;
        }
        if (distance1 == distance2) {
            if (i1 < i2)
                return 1;
            return 2;
        }
        if (distance1 > distance2)
            return 2;
        else return 1;
    }
}