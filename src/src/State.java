package src;

import java.util.Arrays;

public class State {

    private int cost;
    private int produced=0;
    private String[][] puzzle;
    private String move_made;
    private State father;
    private boolean visited;
    private String[] index; // ["2,3","6,9"]

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
        return Arrays.deepEquals(this.puzzle,s.puzzle);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(puzzle);
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

    public void setProduced(int p){
        this.produced=p;
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
}
