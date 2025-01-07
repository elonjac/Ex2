package ex2;

import java.util.Objects;

public class CellRef {
    private final int x;
    private final int y;
    public CellRef(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean equals(CellRef c) {
        if(this == c){
            return true;
        }
        if(c == null || getClass() != c.getClass()){
            return false;
        }
        CellRef that = (CellRef) c;
        return x == that.x && y == that.y;
    }
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
