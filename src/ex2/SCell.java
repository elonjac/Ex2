package ex2;
// Add your documentation below:


public class SCell implements Cell {
    private String line;
    private int type;
    // Add your code here
    private int order;

    public SCell(String s) {
        // Add your code here
        setData(s);
        setOrder(0);
    }

    @Override
    public int getOrder() {
        // Add your code here
        return order;

        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        // Add your code here
        line = s;
        if( line == null || s.trim().isEmpty()) {
            type = Ex2Utils.TEXT;
        }
        if(isNumber(line)){
            type = Ex2Utils.NUMBER;
        }
        else if (isForm(line)) {
            type = Ex2Utils.FORM;
        }
        else if (isText(line)) {
            type = Ex2Utils.TEXT;
        }
        else {
            type = Ex2Utils.ERR_FORM_FORMAT;

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
        order = t;

    }
    public static boolean isNumber(String text){
        if(text == null || text.isEmpty()){
            return false;
        }
        try{
             Double.parseDouble(text);
        }
        catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    public static boolean isForm(String text){

        if(text == null || text.isEmpty()){
            return false;
        }
        return text.matches("(?i)=[a-z]+\\d.*|=.*[\\-+*/()].*");

    }
    public static boolean isText(String text){

        if(isNumber(text)){
            return false;
        }

        if (isForm(text)){
            return false;
        }
        return true;

    }
    public static boolean isCellRef(String form){

        String regex ="[A-Za-z]+[0-9]+";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(form);
        while(m.find()){
            String cellRef = m.group();
            int[] index = parseCellRef(cellRef);
            if(index != null){
                return true;
            }

        }
        return false;
    }
    public static int[] parseCellRef(String cellRef){
        int column = 0;
        int row = 0;
        for(int i=0; i<cellRef.length(); i++){
            char c = cellRef.charAt(i);

            if(Character.isDigit(c)){
                row = Integer.parseInt(cellRef.substring(i));
                break;
            }
            else{
                column = column * 26 + (Character.toUpperCase(c) - 'A' );
            }
        }
        return new int[]{column , row};
    }


}








