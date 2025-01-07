package ex2;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if(cellEntry.isValid()) {
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
        int[][] ans = new int[width()][height()];
        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                eval(i,j);
            }
        }

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

        boolean[][] visit = new boolean[width()][height()];
        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                ans[i][j] = -1;
                visit[i][j] = false;
            }
        }
        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                Cell c = get(i, j);
                if(c != null) {
                    if(c.getType() == Ex2Utils.TEXT || c.getType() == Ex2Utils.NUMBER){
                        ans[i][j] = 0;
                    }
                    else if(c.getType() == Ex2Utils.FORM) {

                        ans[i][j] = calcDep(i,j,visit,ans);
                    }
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
        if (get(x, y) != null){
            ans=get(x, y).toString();
        }else {
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
    private int calcDep(int i, int j, boolean[][] visit , int[][] ans) {
        if(visit[i][j]) {
            return Ex2Utils.ERR_CYCLE_FORM;
        }
        visit[i][j] = true;
        Cell c = get(i, j);
        if(c.getType() != Ex2Utils.FORM || c == null) {
            visit[i][j] = false;
            return 0;
        }
        String form = c.getData();
        List<int[]> refference = SCell.getCellRef(form);
        int maxDep = 0;

        for(int[] ref : refference) {
            int refX = ref[0];
            int refY = ref[1];
            if(isIn(refX, refY)) {
                if(ans[refX][refY] == -1) {
                    ans[refX][refY] = calcDep(refX,refY,visit,ans);
                }
                if(ans[refX][refY] == Ex2Utils.ERR_CYCLE_FORM) {
                    visit[i][j] = false;
                    return Ex2Utils.ERR_CYCLE_FORM;
                }
                maxDep = Math.max(maxDep,ans[refX][refY]);
            }
        }
        visit[i][j] = false;
        return maxDep + 1;
    }
}

