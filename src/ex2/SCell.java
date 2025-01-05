package ex2;
// Add your documentation below:


public class SCell implements Cell {
    private String line;
    private int type;
    // Add your code here
    private int order;

    public SCell(String s) {
        // Add your code here
        this.line = s;
        this.type=Ex2Utils.TEXT;
        setData(s);
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
        if(s == null){
            throw new NumberFormatException("data is null");
        }
        this.line = s.trim();
        if(isNumber(this.line)){
            this.type = Ex2Utils.NUMBER;
        } else if (isForm(line)) {
            this.type = Ex2Utils.FORM;
        } else if (isText(line)) {
            this.type = Ex2Utils.TEXT;
        }
        else {
            this.type = Ex2Utils.ERR_FORM_FORMAT;
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
        this.order = t;

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
    public static double calc(String text){
        if(!text.startsWith("=")){
            throw new IllegalArgumentException("must star with =");
        }
        text = text.substring(1).replaceAll("\\s","");
        if(text.contains("(")){
            int openIndex = text.lastIndexOf("(");
            int closeIndex = text.indexOf(")", openIndex);
            String innerText = text.substring(openIndex+1, closeIndex);
            double innerResult = calc(innerText);
            String newText = text.substring(0,openIndex)+innerResult+text.substring(closeIndex+1);
            return calc(newText);
        }
        for(int i = text.length()-1; i >= 0; i--){
            char c = text.charAt(i);
            if(c =='+'|| c == '-'){
                double left = calc(text.substring(0, i));
                double right = calc(text.substring(i+1));
                return c =='+' ? left + right: left- right;
            }

        }
        for(int i = text.length()-1; i >= 0; i--){
            char c = text.charAt(i);
            if(c =='*'|| c == '/') {
                double left = calc(text.substring(0, i));
                double right = calc(text.substring(i + 1));
                if (c == '/' && right == 0) {
                    throw new ArithmeticException("divide by zero");
                }
                return c =='*' ? left * right: left / right;
            }
        }
        return Double.parseDouble(text);




    }
    public static boolean isForm(String text){
        if(text == null || text.isEmpty()){
            return false;
        }
        return text.matches("(?i)=[a-z]+\\d.*|=.*[\\-+*/()].*");

    }
    public static boolean isText(String text){
        if(text == null || text.isEmpty()){
            return false;
        }
        if(isNumber(text)){
            return false;
        }
        if (isForm(text)){
            return false;
        }
        return true;

    }


}

// Implement eval (look for help online, google/youtube it 'how to write  java recursion for math equations')
// implement set data - update the cell type after setting the line (implement isNumber, isText, isForm) the answer will be the type. Once
// you know the answer, use set type to set the type.
