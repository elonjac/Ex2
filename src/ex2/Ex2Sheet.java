package ex2;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    // Add your code here

    // ///////////////////
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL); //  imitialize all cells as empty
            }
        }
        eval();  //Evaluate the sheet after initialization.
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        // Add your code here

            Cell c = get(x, y);
            if (c != null) {
                ans = eval(x, y);
                if(ans == Ex2Utils.ERR_CYCLE) {
                    table[x][y].setType(Ex2Utils.ERR_CYCLE_FORM); //mark as cycle error

                }
            }


        /////////////////////
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        // Add your code here
       if (cords != null){
           int x = CellEntry.letterToNum(cords.charAt(0)); // turn colomn from letter to number
           int y = Integer.parseInt(cords.substring(1)); // parse row number from string
           ans = get(x, y); // get the cell with the infomation we found above
        }
        /////////////////////
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {

        // Add your code here
        if (isIn(x, y)) {    // if is within bounds
            Cell c = new SCell(s); // create a new cell with the data
            table[x][y] = c; // set the new cell in the table
        }
        /////////////////////
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                eval(i, j); // evaluate each cell
            }

        }


        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx >= 0 && yy >= 0; // check lower bounds
        // Add your code here
        if (xx >= width()|| yy >= height()) {
            ans = false;  // checks upper bounds

        }
        return ans;
    }

    @Override
    public int[][] depth() {
        // Add your code here
        int[][] ans = new int[width()][height()];

        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                int dep = compOrder(table[i][j].getData(),i,j,new HashSet<>()); //compute depth
                ans[i][j] = dep;
            }
        }



        // ///////////////////
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] cells = line.split(",",3); // split the line into components
            if (cells.length >= 3) {
                try{
                    int x = Integer.parseInt(cells[0]);
                    int y = Integer.parseInt(cells[1]);
                    String s = cells[2];
                    set(x, y, s); // set yhe cell value in the table
                }catch(NumberFormatException e){

                }
            }
        }
        reader.close();

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("I2CS ArielU: SpreadSheet\n");
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                Cell c = get(i, j);
                if(c != null && !Ex2Utils.EMPTY_CELL.equals(c.getData())){
                    writer.write(i + "," + j + "," + c.getData() + "\n");
                }
            }
        }
        writer.close();

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        Cell cell = get(x, y);
        if (cell == null || cell.getData().isEmpty() || cell.getData() == null) {
            return Ex2Utils.EMPTY_CELL; // return if cell is null or has no data
        }
        String data = cell.getData();



        if(SCell.isText(data) && !data.startsWith("=") ){ // checks if data is a text and not in a formula
            table[x][y].setType(Ex2Utils.TEXT); // sets type to text
            return data; //returns the text value
        }

        data = data.replaceAll("\\s+",""); // remove all spaces

        if(SCell.isNumber(data)){  // checks if data is a number
            Double d = Double.parseDouble(data); // parces number
            table[x][y].setType(Ex2Utils.NUMBER); // sets type as number
            return String.valueOf(d);  //returns the number value as a string
        }
        data = data.toUpperCase(); // changes the data to uppercase


        if(data.startsWith("(") && data.endsWith(")") && SCell.isNumber(data.substring(1,data.length()-1))){ // checks the number inside parentheses
            Double dataNum = Double.parseDouble(data.substring(1,data.length()-1)); // extracts parse number
            table[x][y].setType(Ex2Utils.NUMBER); // sets type as number
            return String.valueOf(dataNum); //return the number value as a string
        }


        if(SCell.isForm(data)){ // checks if data is a formula
            if(hasConsecutiveOperaters(data)){ //checks if there are consecutive operaters
                table[x][y].setType(Ex2Utils.ERR_FORM_FORMAT); // changes type to error format
                return Ex2Utils.ERR_FORM; // returns err form
            }
            double res = compForm(data); // compute result of the formula
            if(res == Double.NEGATIVE_INFINITY){ // checks for cycle
                return Ex2Utils.ERR_CYCLE; // returns cycle error
            }

            if(!Double.isInfinite(res)){ // if the result is valid set type and return value
                table[x][y].setType(Ex2Utils.FORM);  // set type as formula
                return String.valueOf(res); //return computed result
            }



        }
        if(compOrder(data,x,y,new HashSet<>()) == -1) { // checks for circuler dependencies
            table[x][y].setType(Ex2Utils.ERR_CYCLE_FORM); // set type as cycle error
            return Ex2Utils.ERR_CYCLE;
        }

        table[x][y].setType(Ex2Utils.ERR_FORM_FORMAT); // if none of the above happen set type to error fomat
        return Ex2Utils.ERR_FORM;





    /////////////////////
    }
    private ArrayList<String> dependencies(String form){

        ArrayList<String> dep = new ArrayList<>();
        if(form == null ||form.isEmpty()){
            return dep; // return an empty list if the formula is null or empty
        }

        if(form.startsWith("=")){
            form = form.substring(1); //remove the "=" if the string starts with one
        }

        String[] split = form.split("[+\\-*/()]"); // split the formula into componets using math operaters and parentesis
        for(int i = 0; i < split.length; i++){
            if(SCell.isCellRef(split[i])){ // check if the component is a cell reference
                dep.add(split[i]); // add cell reference to a dependencie list
            }
        }
        return dep; // return the list of dependencies
    }

    int compOrder(String text,int x, int y,HashSet<String> used){

        if(text == null ||text.isEmpty()){
            return 0;  // return 0 if null or empty
        }

        if(SCell.isNumber(text) || SCell.isText(text)){
            return 0; // return  0 if type is text or number
        }

        if(text.startsWith("=")) {
            text = text.substring(1); // remove the "=" if it starts with one
        }

        String cellRef =(char)('A'+ x) + String.valueOf(y); // generate the cell reference
        if(used.contains(cellRef)){
            return Ex2Utils.ERR_CYCLE_FORM; // return err cycle form if circuler dependency  is detected
        }

        used.add(cellRef); // mark the cell as already been used
        ArrayList<String> dependent = dependencies(text); // get dependencies
        int maxOrder = 0;

        for(String dep : dependent){
            int depX =CellEntry.letterToNum(dep.charAt(0)); // change column to number
            int depY = Integer.parseInt(dep.substring(1)); // parse row number

            if(!isIn(depX, depY)){
                continue; // skip out of bounds dependencies
            }

            SCell depCell = (SCell)get(depX, depY); // get dependent cell
            int DepOrder = compOrder(depCell.getData(),depX,depY,used); // comute order recorsivly
            if(DepOrder == -1){
                return Ex2Utils.ERR_CYCLE_FORM; // return err cycle form if a cycle is detected
            }

            maxOrder = Math.max(maxOrder, DepOrder); // update max dependencies
        }

        used.remove(cellRef); // unmark this cell after processing
        return maxOrder +1; // return the computed dependency order

    }
    public Double compForm(String text){

        if(text == null || text.isEmpty()){
            return Double.POSITIVE_INFINITY; //return infinity for empty or null input
        }

        if(text.startsWith("=")){
            text = text.substring(1); //remove "=" if starts with one
        }

        if(SCell.isNumber(text)){
            return Double.parseDouble(text); // return the number directly
        }

        ArrayList<Character> oper = new ArrayList<>();
        ArrayList<Double> num = new ArrayList<>();
        text = text.replaceAll("\\s+",""); // remove spaces
        String[] splitParts = text.split("((?=[-+*/])|(?<=[-+*/]))(?![^()]*\\))"); // splits string into parts based on math operaters while still preseving them

        for(int i = 0; i < splitParts.length; i++){
            String part = splitParts[i];
            if(SCell.isNumber(part)){
                num.add(Double.parseDouble(part)); // parse number and add to list
            }

            if(part.startsWith("(")){
                num.add(compForm(part.substring(1,part.length()-1))); // recursivly evaluate nested formualas
            }
            else if(SCell.isCellRef(part)){
                int refX = CellEntry.letterToNum(part.charAt(0));
                int refY = Integer.parseInt(part.substring(1));
                SCell cell = (SCell)get(part);
                String cellRef = cell.getData();

                if(compOrder(cellRef,refX,refY,new HashSet<>()) == Ex2Utils.ERR_CYCLE_FORM){
                    return Double.NEGATIVE_INFINITY;  // return negetive infinity for circuler dependencies
                }

                String refVal = eval(refX,refY);  // evaluate referenced cell
                if(SCell.isNumber(refVal)){
                    num.add(Double.parseDouble(refVal)); // add the evaluated value if its a number
                }
                else{
                    return Double.POSITIVE_INFINITY; // else return infinity if the reference is invalid
                }
            }

            if(part.length() == 1 && isOper(part.charAt(0))){
                oper.add(part.charAt(0)); // add operaters to the list
            }
        }
        // calculate multipication and division first
        for(int i = 0; i < oper.size(); i++){
            char operChar = oper.get(i);
            if(operChar == '*'){
                double res = num.get(i) * num.get(i+1);
                num.set(i,res); // replace the left number in the calculated part with result
                oper.remove(i); // remove the operater
                num.remove(i+1); // remove the right number in the calculated part
                i--;
            }

            if(operChar == '/'){
                double res = num.get(i) / num.get(i+1);
                num.set(i,res); // replace the left number in the calculated part with result
                oper.remove(i);  // remove the operater
                num.remove(i+1);  // remove the right number in the calculated part
                i--;
            }
        }
        // calculate addition and subtraction after
        for(int i = 0; i < oper.size(); i++){
            char operChar = oper.get(i);
            if(operChar == '+'){
                double res = num.get(i) + num.get(i+1);
                num.set(i,res);  // replace the left number in the calculated part with result
                oper.remove(i);  // remove the operater
                num.remove(i+1);  // remove the right number in the calculated part
                i--;
            }

            if(operChar == '-'){
                double res = num.get(i) - num.get(i+1);
                num.set(i,res); // replace the left number in the calculated part with result
                oper.remove(i);// remove the operater
                num.remove(i+1);  // remove the right number in the calculated part
                i--;
            }
        }
        try{
            return num.get(0); // return the final result
        }catch(Exception e){
            return Double.POSITIVE_INFINITY; // return infinit if there was an error
        }
    }
    public boolean isOper(char operChar){  // checks if there is an opperater

        if(operChar == '+' || operChar == '-' || operChar == '*' || operChar == '/'){
            return true;
        }

        return false;
    }
    public boolean hasConsecutiveOperaters(String text){ // checks if there is consecutive operater
        String oper = "+-*/";
        for(int i = 0; i < text.length()-1; i++){
            if(oper.indexOf(text.charAt(i)) != -1 && oper.indexOf(text.charAt(i+1))!= -1){
                return true; // return true if there are consecutive operaters
            }
        }
        return false;
    }

}
