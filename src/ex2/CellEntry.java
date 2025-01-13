package ex2;
// Add your documentation below:

public  class CellEntry  implements Index2D {
    private int x; // column prsented as a letter
    private int y; // row presented as a number

    public CellEntry(int x, int y) {  // constructer to initialize the cell entry with column x and row y
        this.x = x;
        this.y = y;

    }

    @Override
    public boolean isValid() { // checks if the cell entry is valid.
        if( y >= 0 && y<=99 && x>=0 && x<=99 ){
            return true;
        }
        return false;
    }

    @Override
    public int getX() { // get the column
        if(isValid()) {
            return x; // change it to a number
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
        int temp;// Temporary variable to hold the column index
        temp = numToLetter(x);  // change num to letter value
        char letter = (char) temp; //change the letter value to the actual letter
        return letter + Integer.toString(y); // cobine column and row
    }
    public static int letterToNum(char c) {     // Converts a column letter to numeric index
        c=Character.toUpperCase(c); // convert letter to uppercase
        return c -'A'; // calculate the 0 based number
    }
    public static char numToLetter(int x){
        if(x>=0 && x<=25){
            return (char) ('A' +x);
        }
        else{
            throw new IllegalArgumentException("input not valid");
        }
    }
}
