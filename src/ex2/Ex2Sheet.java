package ex2;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        if (isIn(x, y)) {
            Cell c = get(x, y);
            if (c != null) {
                ans = eval(x, y);
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
        CellEntry cellEntry = new CellEntry(cords);
        if (cellEntry.isValid()) {
            int x = cellEntry.getX();
            int y = cellEntry.getY();
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
        int[][] ans = new int[width()][height()];
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
        if (xx >= table.length || yy >= table[0].length || !ans) {
            ans = false;

        }
        return ans;
    }

    @Override
    public int[][] depth() {
        // Add your code here
        int[][] ans = new int[width()][height()];
        boolean[][] visited = new boolean[width()][height()];
        boolean[][] onStack = new boolean[width()][height()];

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                ans[i][j] = 0;
            }
        }
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (!visited[i][j]) {
                    // Start depth calculation for cell (i, j)
                    calcDepth(i, j, ans, visited, onStack);
                }
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
                    continue;
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
        String ans = null;
        if (get(x, y) != null) {
            ans = get(x, y).toString();
        } else {
            return Ex2Utils.ERR_FORM;
        }
        // Add your code here
        Cell c = get(x, y);
        if(c != null) {
            if(c.getType() == Ex2Utils.NUMBER){
                try{
                    double num = Double.parseDouble(c.getData());
                    ans = String.valueOf(num);
                }catch (Exception e){
                    ans = Ex2Utils.ERR_FORM;
                }
            }else if (c.getType() == Ex2Utils.FORM) {
                try {
                    String formula = c.getData().substring(1);
                    for(int col = 0; col < width(); col++) {
                        for(int row = 0; row < height(); row++) {
                            String ref = Ex2Utils.ABC[col] + row;
                            if(formula.contains(ref)) {
                                String cellVal = eval(col, row);
                                formula = formula.replace(ref, cellVal);
                            }
                        }
                    }

                    double result = SCell.calc(formula);
                    ans = String.valueOf(result);
                }catch (Exception e){
                    ans = Ex2Utils.ERR_FORM;
                }

            }else if (c.getType() == Ex2Utils.TEXT) {
                ans = c.getData();
            }else {
                ans =Ex2Utils.EMPTY_CELL;
            }
        }


    /////////////////////
        return ans;
    }

    private boolean calcDepth(int x, int y, int[][] ans, boolean[][] visited, boolean[][] onStack) {
        if(onStack[x][y]){

            ans[x][y] = -1;
            return true;
        }
        if(visited[x][y]){
            return false;
        }
        visited[x][y] = true;
        onStack[x][y] = true;
        Cell c = get(x, y);
        String data = c != null ? c.getData() : null;
        if(data.startsWith("=")){
            String form = data.substring(1);
            for(int col = 0; col < width(); col++){
                for(int row = 0; row < height(); row++){
                    String ref = Ex2Utils.ABC[col] + row;
                    if (form.contains(ref)) {

                        if(calcDepth(col, row, ans, visited, onStack)){
                            return true;
                        }
                        ans[x][y] = Math.max(ans[x][y],ans[col][row] +1);
                    }
                }
            }
        }
        else {
            ans[x][y] = 0;
        }
        onStack[x][y] = false;
        return false;
    }
}
