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
    //ASSUME NO STRINGS OR CHARACTERS CONTAIN '#', '(', or ')'
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
                        //change number of parencount
                        if (c == Keywords.OPEN_PAREN_KEYWORD) {
                            parenCount++;
                        }
                        if (c == Keywords.CLOSE_PAREN_KEYWORD) {
                            parenCount--;
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
                        if (c == Keywords.STRING_LITERAL_KEYWORD) {
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
                    if (isAlpha(c)) {
                        tokenType = TokenType.VarFunc;
                        
                        while (isAlpha(c)) {
                            currentToken += c;
                            i++;
                            c = line.charAt(i);
                            
                        }
                        // function name
                        if (c == Keywords.OPEN_PAREN_KEYWORD) {
                            tokenType = TokenType.Function;
                            parenCount = 1;
                            i++;
                            while (true) {
                                c = line.charAt(i);
                                //change number of parencount
                                if (c == Keywords.OPEN_PAREN_KEYWORD) {
                                    parenCount++;
                                }
                                if (c == Keywords.CLOSE_PAREN_KEYWORD) {
                                    parenCount--;
                                }
                                if (parenCount == 0) {
                                    break;
                                }
                                currentToken += c;
                                i++;
                            }
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
                    else if (isDigit(c) || c == '-') {
                        tokenType = TokenType.Number;
                        while (isDigit(c)) {
                            currentToken += c;
                            i++;
                            c = line.charAt(i);
                        }
                        //is double
                        if (c == '.') {
                            i++;
                            c = line.charAt(i);
                            while (isDigit(c)) {
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


    //ily calvin
    //calvin++

    //ily calvin 3000
    //           ||||                                                        number
    //env.getVariable("i").setData(env.getVariable("i").addTo(new Variable(          )).getData());
    //calvin += 3000

    //ily = Keywords.INCREMENT_KEYWORD
    public void increment(Environment env, String line) {
        int index = 0;
        for (int i = 0; i < line.length() - 1; i++) {
            if (line.charAt(i))
            variableIndex = line.subString(Keywords.INCREMENT_KEYWORD.length() + 1, )
        }

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
                expression = expression.substring(0, open) + interpretNumberExpression(expression.substring(open + 1, close), env) + expression.substring(close);
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
    public static boolean isAlpha(char c) {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        return (alpha.contains(c + "") || alpha.toUpperCase().contains(c + ""));
    }
    public static boolean isDigit(char c) {
        if ("0123456789".contains(c + "")) {
            return true;
        }
        return false;
    }

    public static String preprocess(String line) {
        return line;
        // int i = 0;
        // String out = "";
        // while (i < line.length()) {
        //     char c = line.charAt(i);
        //     if (c == Keywords.ESCAPE_CHARACTER_KEYWORD) {
        //         i++;
        //         c = line.charAt(i);
        //     }
        // }
    }
}
