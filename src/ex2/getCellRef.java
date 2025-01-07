package ex2;

import java.util.Objects;

public class getCellRef {
    private final int x;
    private final int y;
    public getCellRef(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean equals(getCellRef c) {
        if(this == c){
            return true;
        }
        if(c == null || getClass() != c.getClass()){
            return false;
        }
        getCellRef that = (getCellRef) c;
        return x == that.x && y == that.y;
    }
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
