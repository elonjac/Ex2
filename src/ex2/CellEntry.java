package ex2;
// Add your documentation below:

public  class CellEntry  implements Index2D {
    private char x; // column prsented as a letter
    private char y; // row presented as a number

    public CellEntry(char x, char y) {  // constructer to initialize the cell entry with column x and row y
        this.x = x;
        this.y = y;

    }

    @Override
    public boolean isValid() { // checks if the cell entry is valid
        if( y >= 0 && y<=99 && Character.isLetter(x) ){
            return true;
        }
        return false;
    }

    @Override
    public int getX() { // get the column
        if(isValid()) {
            return letterToNum(x); // change it to a number
        }
        return Ex2Utils.ERR;
    }

    @Override
    public int getY() { // get the row
        if(isValid()) {
            return y;
        }
        return Ex2Utils.ERR;
    }
    public String toString() { // converts the cell entry tova string
        if(!isValid()){
            return Ex2Utils.ERR_FORM;
        }
        StringBuilder sb = new StringBuilder(); // Use StringBuilder for efficient string manipulation
        int temp = x;  // Temporary variable to hold the column index
        while(temp > 0){
            sb.insert(0,(char) ('A' + (temp % 26))); // convert numeric column index to letter
            temp = temp / 26; // move to the next sigificant digit
        }
        return sb + Integer.toString(y); // cobine column and row
    }
    public static int letterToNum(char c) {     // Converts a column letter to numeric index
        c=Character.toUpperCase(c); // convert letter to uppercase
        return c -'A'; // calculate the 0 based number
    }
}
