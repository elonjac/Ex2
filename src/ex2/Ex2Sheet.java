package ex2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
                table[i][j] = new SCell("");
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
                ans = c.toString();
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
        if (cords == null || cords.isEmpty()) {
            return ans;
        }
        char column = cords.charAt(0);
        String row = cords.substring(1);
        int colindex = column - 'A';
        int  rowindex = Integer.parseInt(row) ;

        if (!isIn(rowindex, colindex)) {
            return ans;
        }
        ans = get(colindex, rowindex);

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
        if(isIn(x, y)) {
            Cell c = new SCell(s);
            table[x][y] = c;

        }


        /////////////////////
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here



        // ///////////////////
    }

    @Override
    public  boolean isIn(int xx, int yy) {
        boolean ans = xx >= 0 && yy >= 0;
        // Add your code here
        if (xx >= table.length || yy >= table[0].length || !ans) {
            ans = false;

        }
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here
        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                Cell cell = get(i, j);
                String data = cell.getData().trim();
                if(data.startsWith("=")){
                    String formula = data.substring(1);
                    String[] parts = formula.split("[+\\-*/()]");
                    int maxDepth = 0;

                    for(String part : parts) {
                        part = part.trim();
                        if(part.matches("[A-Z]+[0-9]+")) {
                            int col = part.charAt(0) - 'A';
                            int row = Integer.parseInt(part.substring(1));

                            if(isIn(row, col)) {
                                maxDepth= Math.max(maxDepth, ans[row][col]);
                            }
                        }
                    }
                    ans[i][j] = maxDepth + 1;
                    cell.setOrder(ans[i][j]);
                }
                else{
                    ans[i][j] = 0;
                    cell.setOrder(ans[i][j]);
                }
            }
        }




        // ///////////////////
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        if(get(x,y)!=null) {
            String data = get(x, y).getData();

            // Add your code here
            if (ans.startsWith("=")) {
                try {
                    String formula = data.substring(1);
                    for (int i = 0; i < formula.length(); i++) {
                        if (Character.isLetter(formula.charAt(i))) {
                            int end = i + 1;
                            while (end < formula.length() || Character.isLetter(formula.charAt(end))) {
                                end++;
                            }
                            if (end > i + 1) {
                                String cellRef = formula.substring(i, end);
                                Cell referenced = get(cellRef);
                                if (referenced != null) {
                                    String value = referenced.getData();
                                    formula = formula.substring(0, i) + value + formula.substring(end);
                                }
                            }
                        }
                    }
                    double result = SCell.calc(ans);
                    ans = String.valueOf(result);
                }catch (NumberFormatException e) {
                    ans = Ex2Utils.ERR_FORM;
                }

            }else {
                ans = data;
            }
        }

        /////////////////////
        return ans;
        }
}
