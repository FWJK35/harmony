import java.util.*;

public class StaticMethods {
    
    private enum TokenType {
        None, Variable, Function, Integer, Double, String, Array, Expression, StringInExpression, VarFunc, Number
    }

    public static void print(String line, Environment env) {
        // get beginning of string literal and trim line
        int beginIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD);
        int endIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD, beginIndex + 1);
        String remaining = line.substring(endIndex + 1);
        //System.out.println(beginIndex + " " + endIndex + remaining);
        if (remaining.length() > 0) {
            throw new Error("Invalid Arguments to command: " + Keywords.PRINT_KEYWORD);
        }
        else {
            System.out.println(interpretExpression(line, env));
        }
    }
    
    public static int countIndent(String line) {
        int indent = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                indent++;
            }
            else {
                break;
            }
        }
        return indent;
    }

    public static Variable getTokenVariable(String token) {
        try {
            return new Variable(Integer.parseInt(token));
        } catch (Exception e) {}
        try {
            return new Variable(Double.parseDouble(token));
        } catch (Exception e) {}
        return new Variable(token);
    }

    //xd #My name is # name # and my age is # 69
    public static Variable interpretExpression(String line, Environment env) {
        Variable result = new Variable();
        List<String> tokenValues = new ArrayList<String>();
        List<TokenType> tokenTypes = new ArrayList<TokenType>();
        List<Variable> tokenVariables = new ArrayList<Variable>();

        String currentToken = "";
        TokenType tokenType = TokenType.None;
        int parenCount = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            //check for start of token
            if (tokenType == TokenType.None) {
                //begin nested expression token
                if (c == Keywords.OPEN_PAREN_KEYWORD) {
                    tokenType = TokenType.Expression;
                    parenCount = 1;
                    i++;
                    while (true) {
                        c = line.charAt(i);
                        //check for parenthesis inside string
                        if (c == Keywords.STRING_LITERAL_KEYWORD && line.charAt(i - 1) != '\\') {
                            if (tokenType == TokenType.Expression) {
                                tokenType = TokenType.StringInExpression;
                            }
                            else if (tokenType == TokenType.StringInExpression) {
                                tokenType = TokenType.Expression;
                            }
                        }
                        //change number of parencount
                        if (tokenType != TokenType.StringInExpression && (c == Keywords.OPEN_PAREN_KEYWORD || c == Keywords.CLOSE_PAREN_KEYWORD)) {
                            parenCount += c == Keywords.OPEN_PAREN_KEYWORD? 1:-1;
                        }
                        if (parenCount == 0) {
                            break;
                        }
                        currentToken += c;
                        i++;
                    }
                    tokenValues.add(currentToken);
                    tokenTypes.add(tokenType);
                    tokenType = TokenType.None;
                }
                //begin string literal expression
                else if (c == Keywords.STRING_LITERAL_KEYWORD) {
                    tokenType = TokenType.String;
                    i++;
                    while (true) {
                        c = line.charAt(i);
                        //check for parenthesis inside string
                        if (c == Keywords.STRING_LITERAL_KEYWORD && line.charAt(i - 1) != '\\') {
                            break;
                        }
                        currentToken += c;
                        i++;
                    }
                    tokenValues.add(currentToken);
                    tokenTypes.add(TokenType.String);
                    currentToken = "";
                    tokenType = TokenType.None;
                }
                // must be variable, function, or int
                else {
                    // character is alphabet character
                    if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                        tokenType = TokenType.VarFunc;
                        
                        while ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                            currentToken += c;
                            i++;
                            c = line.charAt(i);
                            
                        }
                        // function name
                        if (c == Keywords.OPEN_PAREN_KEYWORD) {

                        }
                        // array index
                        else if (c == Keywords.OPEN_ARRAY_KEYWORD) {

                        }
                        // probably a variable (not array)
                        else {
                            tokenValues.add(currentToken);
                            tokenTypes.add(TokenType.Variable);
                            tokenType = TokenType.None;
                        }
                    }
                    //character is digit
                    else if (c >= 48 && c <= 57 || c == '-') {
                        tokenType = TokenType.Number;
                        while (c >= 48 && c <= 57) {
                            currentToken += c;
                            i++;
                            c = line.charAt(i);
                        }
                        //is double
                        if (c == '.') {
                            i++;
                            c = line.charAt(i);
                            while (c >= 48 && c <= 57) {
                                currentToken += c;
                                i++;
                                c = line.charAt(i);
                            }
                        }
                        //get next non-space character
                        while (c == ' ') {
                            i++;
                            c = line.charAt(i);
                        }



                    }
                }
            }
            
        }
        for (int t = 0; t < tokenValues.size(); t++) {
            String currentValue = tokenValues.get(t);
            TokenType currentType = tokenTypes.get(t);

            if (currentType == TokenType.Expression) {
                tokenVariables.add(interpretExpression(currentValue, env));
            }
            else if (currentType == TokenType.String) {
                tokenVariables.add(new Variable(currentValue));
            }
            else if (currentType == TokenType.Variable) {
                if (env.containsVariable(currentValue)) {
                    tokenVariables.add(env.getVariable(currentValue));
                }
                else {
                    throw new Error("Variable name not found");
                }
            }
            else {

            }
        }
        if (tokenValues.size() > 1) {
            for (Variable token : tokenVariables) {
                result = result.addTo(token);
            }
        }
        return result;
    }

    /* TODO read this
     * interpretNumberExpression
     * should take a string containing numbers, variables, and functions
     * containing NO SPACES and use order of operations
     * you can put any expression within parenthesis back into interpretExpression
     * same with really anything between operators
     * for example:
     * 3.5*sqrt(9)+6/(3/2)
     *     |||||||   |||||
     * you can put these sections into interpretExpression
     * have fun :)
     * 
     */

    //you can use interpretExpression on each segment of the expression
    //for example
    //1+2*3-(sqrt(9)/2)
    //| | | ||||||||||| <- this 

    public double interpretNumberExpression(String line, Environment env) {
        if (line.contains(" ")) {
            // TODO: throw some error here
        }
        
        String special = "()^/*+-";
        String expression = line;
        
        while (expression.contains("(") || expression.contains("")) {
            int open = expression.indexOf("(");
            int close = expression.indexOf(")");
            if (open < close && open != -1) {
                expression = expression.substring(0, open) + interpretNumberExpression(expression.substring(open + 1, close), variables, functions) + expression.substring(close);
            }
            else {
                // TODO: throw some error here
            }
        }
        

        // int left = 0;
        // int right = 0;
        // for (char i : line.toCharArray()) {
        //     right++;
        //     if(special.contains(i + "")) {
        //         if (i=='+') {
        //             return Integer.parseInt(line.substring(left,right)) + interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='-') {
        //             return Integer.parseInt(line.substring(left,right)) - interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='*') {
        //             return Integer.parseInt(line.substring(left,right)) * interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='/') {
        //             return Integer.parseInt(line.substring(left,right)) / interpretNumberExpression(line.substring(right), variables, functions);
        //         }
        //         else if (i=='-') {
        //             return Math.pow(Integer.parseInt(line.substring(left,right)), interpretNumberExpression(line.substring(right), variables, functions));
        //         }
        //         else if (i=='(') {
        //             left = right;
        //             right = line.indexOf(")");
                    
        //         }
        //     }
        // }
        return 0.0;
    }
    
    // replaces all variable and functions with proper bits
    // private String preprocessing(String line) {
    //     String[] separate = line.replace(Keywords.ESCAPE_CHARACTER_KEYWORD + Keywords.STRING_LITERAL_KEYWORD).split("#");

    // }
    
}
