package ex2;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private char x;
    private char y;

    public CellEntry(char x, char y) {
        this.x = x;
        this.y = y;

    }

    @Override
    public boolean isValid() {
        if(Character.isLetter(x) && y >= 0 && y<=99 ){
            return true;
        }
        return false;
    }

    @Override
    public int getX() {
        if(isValid()) {
            return letterToNum(x);
        }
        return Ex2Utils.ERR;
    }

    @Override
    public int getY() {
        if(isValid()) {
            return y;
        }
        return Ex2Utils.ERR;
    }
    public String toString() {
        if(!isValid()){
            return "Invalid";
        }
        StringBuilder sb = new StringBuilder();
        int temp = x;
        while(temp > 0){
            sb.insert(0,(char) ('A' + (temp % 26)));
            temp = temp / 26;
        }
        return sb + Integer.toString(y);
    }
    public static int letterToNum(char c) {
        c=Character.toUpperCase(c);
        return c -'A';
    }
}
