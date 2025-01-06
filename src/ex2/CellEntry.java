package ex2;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private int x;
    private int y;

    public CellEntry(String index) {
        if(index == null || index.isEmpty()) {
            this.x = Ex2Utils.ERR;
            this.y = Ex2Utils.ERR;
        }
        else{
            parseIndex(index);
        }
    }
    private void parseIndex(String index) {
        int col = 0;
        int i = 0;
        while (i < index.length() &&Character.isLetter(index.charAt(i))) {
            col = col * 26 + (Character.toUpperCase(index.charAt(i)) - 'A' +1);
        }
        int row = Integer.parseInt(index.substring(i));
        this.x = col;
        this.y = row;
    }
    private boolean isValidIndex(String index) {
        if(index != null && index.matches("(?i)[A-Z]+\\d{1,2}")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isValid() {
        if(this.x != Ex2Utils.ERR && this.y != Ex2Utils.ERR){
            return true;
        }
        return false;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
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
}
