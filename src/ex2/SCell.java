package ex2;
// Add your documentation below:


public class SCell implements Cell {
    private String line;  // represents the data of the cell
    private int type; // represents the type of cell
    // Add your code here
    private int order;   // represents the computational order of the cell

    public SCell(String s) {
        // Add your code here
        setData(s); // set the cell with data and type
        setOrder(0); // default computational order is 0
    }

    @Override
    public int getOrder() {  //  return the computational order of cell
        // Add your code here
        return order; // return the pre computed order of cell

        // ///////////////////
    }

    //@Override
    @Override
    public String toString() { // converts cell content to string
        return getData(); // return cells data
    }

    @Override
public void setData(String s) { // sets the cells data and finds its type
        // Add your code here
        line = s;
        if( line == null || s.trim().isEmpty()) {
            type = Ex2Utils.TEXT; // empty or null is text
        }
        if(isNumber(line)){
            type = Ex2Utils.NUMBER; // if the data is a valid number
        }
        else if (isForm(line)) {
            type = Ex2Utils.FORM; // if the data is a formula
        }
        else if (isText(line)) {
            type = Ex2Utils.TEXT; // if the data is a text
        }
        else {
            type = Ex2Utils.ERR_FORM_FORMAT; // invalid format

        }

        /////////////////////
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here
        order = t;  // assign the order used for dependency calculations

    }
    public static boolean isNumber(String text){  // checks if a string is a valid number
        if(text == null || text.isEmpty()){
            return false; // null or empty return false
        }
        try{
             Double.parseDouble(text); // try parsing the string to a number
        }
        catch(NumberFormatException e){
            return false; // return false if fails
        }
        return true; // successfully parsed as a number
    }
    public static boolean isForm(String text){ // checks if the string is a valid formula
        if(text == null || text.isEmpty()){
            return false; // if null or empty not a valid formual
        }
        if(text.matches("(?i)=[a-z]+\\d.*|=.*[\\-+*/()].*")){  // Matches strings that begin with "=" and have valid operations or cell references
            return true;
        }
        if(text.startsWith("=")){  // Any string starting with "=" is a formula
            return true;
        }
        return false;

    }
    public static boolean isText(String text){    // Checks if the string is a text (not a number or formula)

        if(isNumber(text)){
            return false;  // Numbers are not considered text
        }

        if (isForm(text)){
            return false;   // Formulas are not considered text
        }
        return true;  // Checks if the string is a text (not a number or formula)

    }
    public static boolean isCellRef(String form){    // Checks if the string contains valid cell reference

        String regex ="[A-Za-z]+[0-9]+";   // Regex for cell references
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(form);
        while(m.find()){
            String cellRef = m.group();   // Extract the matched cell reference
            int[] index = parseCellRef(cellRef);  // Parse the reference
            if(index != null){
                return true;  // If parsing is successful, it's a valid cell reference
            }

        }
        return false; // not valid reference
    }
    public static int[] parseCellRef(String cellRef){  // Parses a cell reference
        int column = 0;  // Initialize column index
        int row = 0; // Initialize row index
        for(int i=0; i<cellRef.length(); i++){
            char c = cellRef.charAt(i);  // Process each character in the reference

            if(Character.isDigit(c)){
                row = Integer.parseInt(cellRef.substring(i));  // Extract the row number
                break;
            }
            else{
                column = column * 26 + (Character.toUpperCase(c) - 'A' );  // Compute the column index
            }
        }
        return new int[]{column , row};  // Return the parsed column and row as an array
    }


}








