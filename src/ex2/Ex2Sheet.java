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
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
        eval();
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
                    table[x][y].setType(Ex2Utils.ERR_CYCLE_FORM);

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
           int x = CellEntry.letterToNum(cords.charAt(0));
           int y = Integer.parseInt(cords.substring(1));
           ans = get(x, y);
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
        if (isIn(x, y)) {
            Cell c = new SCell(s);
            table[x][y] = c;
        }
        /////////////////////
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                eval(i, j);
            }
        }

        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx >= 0 && yy >= 0;
        // Add your code here
        if (xx >= width()|| yy >= height()) {
            ans = false;

        }
        return ans;
    }

    @Override
    public int[][] depth() {
        // Add your code here
        int[][] ans = new int[width()][height()];

        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                int dep = compOrder(table[i][j].getData(),i,j,new HashSet<>());
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
            String[] cells = line.split(",",3);
            if (cells.length >= 3) {
                try{
                    int x = Integer.parseInt(cells[0]);
                    int y = Integer.parseInt(cells[1]);
                    String s = cells[2];
                    set(x, y, s);
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
            return Ex2Utils.EMPTY_CELL;
        }
        String data = cell.getData();


        if(compOrder(data,x,y,new HashSet<>()) == -1) {
            table[x][y].setType(Ex2Utils.ERR_CYCLE_FORM);
            return Ex2Utils.ERR_CYCLE;
        }

        if(SCell.isText(data) && !data.startsWith("=") ){
            table[x][y].setType(Ex2Utils.TEXT);
            return data;
        }

        data = data.replaceAll("\\s+","");
        if(SCell.isNumber(data)){
            Double d = Double.parseDouble(data);
            table[x][y].setType(Ex2Utils.NUMBER);
            return String.valueOf(d);
        }
        data = data.toUpperCase();


        if(data.startsWith("(") && data.endsWith(")") && SCell.isNumber(data.substring(1,data.length()-1))){
            Double dataNum = Double.parseDouble(data.substring(1,data.length()-1));
            table[x][y].setType(Ex2Utils.NUMBER);
            return String.valueOf(dataNum);
        }


        if(SCell.isForm(data)){
            if(hasConsecutiveOperaters(data)){
                table[x][y].setType(Ex2Utils.ERR_FORM_FORMAT);
                return Ex2Utils.ERR_FORM;
            }
            double res = compForm(data);
            if(!Double.isInfinite(res)){
                table[x][y].setType(Ex2Utils.FORM);
                return String.valueOf(res);
            }
            if(res == Double.NEGATIVE_INFINITY){
                return Ex2Utils.ERR_CYCLE;
            }


        }

        table[x][y].setType(Ex2Utils.ERR_FORM_FORMAT);
        return Ex2Utils.ERR_FORM;





    /////////////////////
    }
    private ArrayList<String> dependencies(String form){

        ArrayList<String> dep = new ArrayList<>();
        if(form == null ||form == ""){
            return dep;
        }

        if(form.startsWith("=")){
            form = form.substring(1);
        }

        String[] split = form.split("[+\\-*/()]");
        for(int i = 0; i < split.length; i++){
            if(SCell.isCellRef(split[i])){
                dep.add(split[i]);
            }
        }
        return dep;
    }

    int compOrder(String text,int x, int y,HashSet<String> used){

        if(text == null ||text.isEmpty()){
            return 0;
        }

        if(SCell.isNumber(text) || SCell.isText(text)){
            return 0;
        }

        if(text.startsWith("=")) {
            text = text.substring(1);
        }

        String cellRef =(char)('A'+ x) + String.valueOf(y);
        if(used.contains(cellRef)){
            return -1;
        }

        used.add(cellRef);
        ArrayList<String> dependent = dependencies(text);
        int maxOrder = 0;
        for(String dep : dependent){
            int depX =CellEntry.letterToNum(dep.charAt(0));
            int depY = Integer.parseInt(dep.substring(1));
            if(!isIn(depX, depY)){
                continue;
            }

            SCell depCell = (SCell)get(depX, depY);
            int DepOrder = compOrder(depCell.getData(),depX,depY,used);
            if(DepOrder == -1){
                return -1;
            }

            maxOrder = Math.max(maxOrder, DepOrder);
        }

        used.remove(cellRef);
        return maxOrder +1;

    }
    public Double compForm(String text){

        if(text == null || text.isEmpty()){
            return Double.POSITIVE_INFINITY;
        }

        if(text.startsWith("=")){
            text = text.substring(1);
        }

        if(SCell.isNumber(text)){
            return Double.parseDouble(text);
        }

        ArrayList<Character> oper = new ArrayList<>();
        ArrayList<Double> num = new ArrayList<>();
        text = text.replaceAll("\\s+","");
        String[] splitParts = text.split("((?=[-+*/])|(?<=[-+*/]))(?![^()]*\\))");

        for(int i = 0; i < splitParts.length; i++){
            String part = splitParts[i];
            if(SCell.isNumber(part)){
                num.add(Double.parseDouble(part));
            }

            if(part.startsWith("(")){
                num.add(compForm(part.substring(1,part.length()-1)));
            }
            else if(SCell.isCellRef(part)){
                int refX = CellEntry.letterToNum(part.charAt(0));
                int refY = Integer.parseInt(part.substring(1));
                SCell cell = (SCell)get(part);
                String cellRef = cell.getData();

                if(compOrder(cellRef,refX,refY,new HashSet<>()) == -1){
                    return Double.NEGATIVE_INFINITY;
                }

                String refVal = eval(refX,refY);
                if(SCell.isNumber(refVal)){
                    num.add(Double.parseDouble(refVal));
                }
                else{
                    return Double.POSITIVE_INFINITY;
                }
            }

            if(part.length() == 1 && isOper(part.charAt(0))){
                oper.add(part.charAt(0));
            }
        }

        for(int i = 0; i < oper.size(); i++){
            char operChar = oper.get(i);
            if(operChar == '*'){
                double res = num.get(i) * num.get(i+1);
                num.set(i,res);
                oper.remove(i);
                num.remove(i+1);
                i--;
            }

            if(operChar == '/'){
                double res = num.get(i) / num.get(i+1);
                num.set(i,res);
                oper.remove(i);
                num.remove(i+1);
                i--;
            }
        }
        for(int i = 0; i < oper.size(); i++){
            char operChar = oper.get(i);
            if(operChar == '+'){
                double res = num.get(i) + num.get(i+1);
                num.set(i,res);
                oper.remove(i);
                num.remove(i+1);
                i--;
            }

            if(operChar == '-'){
                double res = num.get(i) - num.get(i+1);
                num.set(i,res);
                oper.remove(i);
                num.remove(i+1);
                i--;
            }
        }
        try{
            return num.get(0);
        }catch(Exception e){
            return Double.POSITIVE_INFINITY;
        }
    }
    public boolean isOper(char operChar){

        if(operChar == '+' || operChar == '-' || operChar == '*' || operChar == '/'){
            return true;
        }

        return false;
    }
    public boolean hasConsecutiveOperaters(String text){
        String oper = "+-*/";
        for(int i = 0; i < text.length()-1; i++){
            if(oper.indexOf(text.charAt(i)) != -1 && oper.indexOf(text.charAt(i+1))!= -1){
                return true;
            }
        }
        return false;
    }

}
