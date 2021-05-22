import java.util.Arrays;

public class State implements Comparable<State> {

    private int cost;
    private int produced = 0;
    private String[][] puzzle;
    private String move_made;
    private State father;
    private boolean visited;
    private String[] index; // ["2,3","6,9"]
    private int F;
    private int H;

    State(String[][] p, int cost, State father, String move_made, String[] idx) {
        this.puzzle = p;
        this.cost = cost;
        this.father = father;
        this.index = new String[2];
        this.visited = false;
        this.move_made = move_made;
        this.index = idx;
    }

    State() {
        this.puzzle = null;
        this.cost = 0;
        this.father = null;
        this.index = new String[2];
        this.visited = false;
        this.move_made = "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this) {
            return true;
        }
        State s = (State) obj;
        return Arrays.deepEquals(this.puzzle, s.puzzle);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(puzzle);
    }

    public void setH(State Goal) {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[0].length; j++) {
                if (!puzzle[i][j].equals("_")) {
                    for (int k = 0; k < puzzle.length; k++) {
                        for (int l = 0; l < puzzle[0].length; l++) {
                            if (Goal.getPuzzle()[k][l].equals(puzzle[i][j])) {
                                this.H += 3*(Math.abs(k - i)+Math.abs(l - j));
                            }
                        }
                    }
                }
            }
        }
    }

    public int getH() {
        return H;
    }

    public void setF() {
        this.F = this.cost + this.getH();
    }

    public int getF() {
        return F;
    }

    public String getMove() {
        return move_made;
    }

    public int getCost() {
        return cost;
    }

    public State getFather() {
        return father;
    }

    public void setProduced(int p) {
        this.produced = p;
    }

    public int getProduced() {
        return produced;
    }

    void setVisited(boolean a) {
        this.visited = a;
    }

    boolean getVisited() {
        return this.visited;
    }

    public void setPuzzle(String[][] puzzle) {
        this.puzzle = puzzle;
    }

    String[][] getPuzzle() {
        return puzzle;
    }

    public void setIndex(String[] index) {
        this.index = index;
    }

    public String[] getIndex() {
        return index;
    }

    @Override
    public int compareTo(State o) {
        if (this.getF() > o.getF())
            return 1;
        if (this.getF() < o.getF())
            return -1;
        return 0;
    }
}
