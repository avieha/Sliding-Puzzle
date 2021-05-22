import java.util.*;

public class Algo {

    private State start;
    private State goal;
    private int empty_blocks = 0;
    private boolean list_show;
    private int N = 0;
    private int M = 0;

    Algo(State s, State g, boolean show) {
        start = s;
        goal = g;
        list_show = show;
        if (s.getIndex()[0] != null)
            empty_blocks++;
        if (s.getIndex()[1] != null)
            empty_blocks++;
        N = s.getPuzzle().length;
        M = s.getPuzzle()[0].length;
    }

    public State BFS(State start, State Goal) {
        HashSet<State> open_list = new HashSet<>();
        LinkedList<State> q = new LinkedList<>();
        int counter = 0;
        open_list.add(start);
        q.add(start);
        while (!q.isEmpty()) {
            State n = q.removeFirst();
//            String[][] sa = {{"1", "3", "4"}, {"2", "5", "6"}, {"_", "7", "_"}};
//            if (Arrays.deepEquals(n.getPuzzle(), sa)) {
//                System.out.println("*****************************************************");
//            }
            n.setVisited(true);
            List<State> list = kids(n);
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
            if (list_show)
                print_list(open_list.iterator());
        }
        return null;
    }

    public State DFID(State start, State Goal) {
        int counter = 0;
        State result = new State();
        for (int limit = 1; limit < Integer.MAX_VALUE; limit++) {
            HashSet<State> h = new HashSet<>();
            result = Limited_DFS(start, Goal, limit, h, counter);
            if (!result.getIndex()[0].equals("Cutoff"))
                return result;
        }
        if (result.getIndex()[0].equals("fail"))
            return null;
        return result;
    }

    private State Limited_DFS(State n, State Goal, int limit, HashSet h, int counter) {
        if (n.equals(Goal)) {
            n.setProduced(counter);
            return n;
        } else if (limit == 0) {
            n.setIndex(new String[]{"Cutoff"});
            return n;
        } else {
            h.add(n);
            boolean isCutoff = false;
            List<State> list = kids(n);
            counter += list.size();
            for (State s : list) {
                if (h.contains(s))
                    continue;
                n = Limited_DFS(s, Goal, limit - 1, h, counter);
                if (n.getIndex()[0].equals("Cutoff"))
                    isCutoff = true;
                else if (!n.getIndex()[0].equals("fail"))
                    return n;
            }
            if (list_show)
                print_list(h.iterator());
            h.remove(n);
            if (isCutoff == true) {
                n.setIndex(new String[]{"Cutoff"});
            } else {
                n.setIndex(new String[]{"fail"});
            }
            return n;
        }
    }

    public State Astar(State start, State Goal) {
        HashSet<State> closed_list = new HashSet<>();
        HashMap<String[][], State> open_list = new HashMap<>();
        PriorityQueue<State> q = new PriorityQueue<>();
        q.add(start);
        open_list.put(start.getPuzzle(), start);
        int counter = 0;
        while (!q.isEmpty()) {
            State s = q.poll();
            if (s.equals(Goal)) {
                s.setProduced(counter);
                return s;
            }
            s.setVisited(true);
            closed_list.add(s);
            if (list_show)
                print_list(open_list.values().iterator());
            List<State> list = kids(s);
            counter += list.size();
            for (State temp : list) {
                if (!closed_list.contains(temp) && !open_list.containsKey(temp.getPuzzle())) {
                    temp.setH(Goal);
                    temp.setF();
                    q.add(temp);
                    open_list.put(temp.getPuzzle(), temp);
                } else if (open_list.containsKey(temp.getPuzzle())) {
                    if (open_list.get(temp.getPuzzle()).getF() >= temp.getF()) {
                        open_list.remove(temp.getPuzzle());
                        open_list.put(temp.getPuzzle(), temp);
                    }
                }
            }
        }
        return null;
    }

    public State IDAstar(State start, State Goal) {
        Stack<State> L = new Stack<>();
        Hashtable<String[][], State> H = new Hashtable<>();
        int counter = 0;
        start.setH(goal);
        int threshold = start.getH();
        while (threshold != Integer.MAX_VALUE) {
            int minF = Integer.MAX_VALUE;
            L.push(start);
            H.put(start.getPuzzle(), start);
            while (!L.isEmpty()) {
                if (list_show)
                    print_list(H.values().iterator());
                State n = L.pop();
                if (n.getVisited()) {
                    H.remove(n.getPuzzle());
                } else {
                    n.setVisited(true);
                    L.push(n);
                    List<State> list = kids(n);
                    counter += list.size();
                    for (State g : list) {
                        g.setH(goal);
                        g.setF();
                        if (g.getF() > threshold) {
                            minF = Math.min(minF, g.getF());
                            continue;
                        }
                        // Loop avoidance
                        if (H.containsKey(g.getPuzzle()) && H.get(g.getPuzzle()).getVisited()) {
                            continue;
                        }
                        if (H.containsKey(g.getPuzzle()) && !H.get(g.getPuzzle()).getVisited()) {
                            if (H.get(g.getPuzzle()).getF() > g.getF()) {
                                L.remove(H.get(g.getPuzzle()));
                                H.remove(g.getPuzzle());
                            } else {
                                continue;
                            }
                        }
                        if (g.equals(goal)) {
                            g.setProduced(counter);
                            return g;
                        }
                        L.push(g);
                        H.put(g.getPuzzle(), g);
                    }
                }
            }
            start.setVisited(false);
            threshold = minF;
        }
        return null;
    }

    public State DFBnB(State start, State Goal) {
        Stack<State> L = new Stack<>();
        Hashtable<String[][], State> H = new Hashtable<>();
        int counter = 0;
        L.add(start);
        H.put(start.getPuzzle(), start);
        State result = null;
        int t = Integer.MAX_VALUE;
        while (!L.isEmpty()) {
            State n = L.pop();
            if (n.getVisited())
                H.remove(n.getPuzzle());
            else {
                n.setVisited(true);
                if (list_show)
                    print_list(H.values().iterator());
                L.push(n);
                List<State> list = kids(n);
                counter += list.size();
                if (list != null) {
                    for (State s : list) {
                        s.setH(Goal);
                        s.setF();
                    }
                }
                Collections.sort(list, State::compareTo);
                ListIterator<State> itr = list.listIterator();
                while (itr.hasNext()) {
                    State g = itr.next();
                    if (g.getF() >= t) {
                        itr.remove();
                        while (itr.hasNext() && list.contains(itr.next())) {
                            itr.remove();
                        }
                    } else if (H.containsKey(g.getPuzzle()) && H.get(g.getPuzzle()).getVisited())
                        itr.remove();
                    else if (H.containsKey(g.getPuzzle()) && !H.get(g.getPuzzle()).getVisited()) {
                        if (H.get(g.getPuzzle()).getF() <= g.getF()) {
                            itr.remove();
                        } else {
                            H.remove(g.getPuzzle());
                            L.remove(H.get(g.getPuzzle()));
                        }
                    } else if (g.equals(Goal)) {
                        t = g.getF();
                        g.setProduced(counter);
                        result = g;
                        itr.remove();
                        while (itr.hasNext() && list.contains(itr.next())) {
                            itr.remove();
                        }
                    }
                }
                Collections.reverse(list);
                for (int i = 0; i < list.size(); i++) {
                    State g = list.get(i);
                    H.put(g.getPuzzle(), g);
                    L.push(g);
                }
            }
        }
        return result;
    }

    private List<State> kids(State father) {
        if (empty_blocks == 1) {
            return one_empty_block(father);
        } else if (empty_blocks == 2) {
            return two_empty_blocks(father);
        }
        return null;
    }

    private List<State> one_empty_block(State father) {
        String[] location;
        List<State> list = new LinkedList<>();
        location = (father.getIndex()[0]).split(","); // "23,4"
        int i = Integer.parseInt(location[0]);
        int j = Integer.parseInt(location[1]);
        if ((j + 1) < M) {
            String[][] first_temp_board = clone_puzzle(father.getPuzzle());
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
            String[][] second_temp_board = clone_puzzle(father.getPuzzle());
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
            String[][] third_temp_board = clone_puzzle(father.getPuzzle());
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
            String[][] fourth_temp_board = clone_puzzle(father.getPuzzle());
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

    private List<State> two_empty_blocks(State father) {
        String[][] board;
        List<State> list = new LinkedList<>();
        String[] first = father.getIndex()[0].split(",");
        String[] second = father.getIndex()[1].split(",");
        int i1 = Integer.parseInt(first[0]); // always the closest to the
        int j1 = Integer.parseInt(first[1]); // top of the matrix
        int i2 = Integer.parseInt(second[0]);
        int j2 = Integer.parseInt(second[1]);
        boolean tight = false;
        if (check_distance(first, second) == 3)
            tight = true;
        // double-vertical twin
        if (i2 > i1) {
            if (tight && (j1 + 1) < M && (j2 + 1) < M) {
                board = clone_puzzle(father.getPuzzle());
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
            if (tight && (j1 - 1) >= 0 && (j2 - 1) >= 0 && tight) {
                board = clone_puzzle(father.getPuzzle());
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
            if ((j1 + 1) < M) {
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i1][j1];
                board[i1][j1] = board[i1][j1 + 1];
                board[i1][j1 + 1] = temp;
                String number_moved = board[i1][j1] + "L";
                State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + (j1 + 1), i2 + "," + j2});
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            if ((i1 + 1) < N && !tight) {
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i1][j1];
                board[i1][j1] = board[i1 + 1][j1];
                board[i1 + 1][j1] = temp;
                String number_moved = board[i1][j1] + "U";
                String[] s;
                if (i1 + 1 >= i2 && j1 > j2) {
                    s = new String[]{i2 + "," + j2, (i1 + 1) + "," + j1};
                } else {
                    s = new String[]{(i1 + 1) + "," + j1, i2 + "," + j2};
                }
                State a = new State(board, father.getCost() + 5, father, number_moved, s);
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            if ((j1 - 1) >= 0) {
                board = clone_puzzle(father.getPuzzle());
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
                board = clone_puzzle(father.getPuzzle());
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
                board = clone_puzzle(father.getPuzzle());
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
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i2][j2];
                board[i2][j2] = board[i2 + 1][j2];
                board[i2 + 1][j2] = temp;
                String number_moved = board[i2][j2] + "U";
                State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, (i2 + 1) + "," + j2});
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            if ((j2 - 1) >= 0) {
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i2][j2];
                board[i2][j2] = board[i2][j2 - 1];
                board[i2][j2 - 1] = temp;
                String number_moved = board[i2][j2] + "R";
                State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, i2 + "," + (j2 - 1)});
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            if ((i2 - 1) >= 0 && !tight) {
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i2][j2];
                board[i2][j2] = board[i2 - 1][j2];
                board[i2 - 1][j2] = temp;
                String number_moved = board[i2][j2] + "D";
                String[] s;
                if (i2 - 1 <= i1 && j2 < j1)
                    s = new String[]{(i2 - 1) + "," + j2, i1 + "," + j1};
                else s = new String[]{i1 + "," + j1, (i2 - 1) + "," + j2};
                State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, (i2 - 1) + "," + j2});
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            return list;
        } else if (j2 > j1) {  // double-horizontal twin
            if (tight && (i1 + 1) < N && (i2 + 1) < N) {
                board = clone_puzzle(father.getPuzzle());
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
            if (tight && (i1 - 1) >= 0 && (i2 - 1) >= 0) {
                board = clone_puzzle(father.getPuzzle());
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
            if ((i1 + 1) < N) {
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i1][j1];
                board[i1][j1] = board[i1 + 1][j1];
                board[i1 + 1][j1] = temp;
                String number_moved = board[i1][j1] + "U";
                String[] s;
                if (i1 + 1 > i2)
                    s = new String[]{i2 + "," + j2, (i1 + 1) + "," + j1};
                else s = new String[]{(i1 + 1) + "," + j1, i2 + "," + j2};
                State a = new State(board, father.getCost() + 5, father, number_moved, s);
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            if ((j1 - 1) >= 0) {
                board = clone_puzzle(father.getPuzzle());
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
                board = clone_puzzle(father.getPuzzle());
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
                board = clone_puzzle(father.getPuzzle());
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
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i2][j2];
                board[i2][j2] = board[i2 + 1][j2];
                board[i2 + 1][j2] = temp;
                String number_moved = board[i2][j2] + "U";
                State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + j1, (i2 + 1) + "," + j2});
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            if ((j2 - 1) >= 0 && !tight) {
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i2][j2];
                board[i2][j2] = board[i2][j2 - 1];
                board[i2][j2 - 1] = temp;
                String number_moved = board[i2][j2] + "R";
                State a = new State(board, father.getCost() + 5, father, number_moved, new String[]{i1 + "," + (j1), i2 + "," + (j2 - 1)});
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            if ((i2 - 1) >= 0) {
                board = clone_puzzle(father.getPuzzle());
                // Swap
                String temp = board[i2][j2];
                board[i2][j2] = board[i2 - 1][j2];
                board[i2 - 1][j2] = temp;
                String number_moved = board[i2][j2] + "D";
                String[] s;
                if (i2 - 1 < i1)
                    s = new String[]{(i2 - 1) + "," + j2, i1 + "," + j1};
                else s = new String[]{i1 + "," + j1, (i2 - 1) + "," + j2};
                State a = new State(board, father.getCost() + 5, father, number_moved, s);
                if (!a.equals(father.getFather()) && !a.equals(father))
                    list.add(a);
            }
            return list;
        }
        return list;
    }

    private void print_list(Iterator<State> itr) {
        int counter = 1;
        System.out.println("********* //////  open list - another iteration  \\\\\\\\\\\\ *********");
        while (itr.hasNext()) {
            String[][] puzzle = itr.next().getPuzzle();
            System.out.println("State num: " + counter++);
            for (int i = 0; i < puzzle.length; i++) {
                for (int j = 0; j < puzzle[i].length; j++) {
                    System.out.print(puzzle[i][j]);
                    if (j != puzzle[0].length - 1)
                        System.out.print(", ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private String[][] clone_puzzle(String[][] puzzle) {
        String[][] board = new String[N][M];
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                board[i][j] = puzzle[i][j];
            }
        }
        return board;
    }

    private int check_distance(String[] first, String[] second) {
        int i1 = Integer.parseInt(first[0]); // 0
        int j1 = Integer.parseInt(first[1]); // 1
        int i2 = Integer.parseInt(second[0]); // 2
        int j2 = Integer.parseInt(second[1]); // 1
        double distance_between = Math.sqrt(Math.pow(i2 - i1, 2) + Math.pow(j2 - j1, 2));
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
