import java.util.*;

public class StaticMethods {
    
    private enum TokenType {
        None, Variable, Number, String, Array, Expression, StringInExpression
    };

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
                    tokenTypes.add(tokenType);
                    currentToken = "";
                    tokenType = TokenType.None;
                }
                else {

                }
            }
            
        }
        for (int t = 0; t < tokenValues.size(); t++) {
            String currentValue = tokenValues.get(t);
            TokenType currentType = tokenTypes.get(t);

            if (currentType == TokenType.Expression) {
                tokenVariables.add(interpretExpression(currentValue, env));
            }
            else if (tokenTypes.get(t) == TokenType.String) {
                tokenVariables.add(new Variable(currentValue));
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

    //TODO: WRITE THIS METHOD
    //1+2*4/6^2-(6+2)

    //1+getlen(#Calvin#)
    public double interpretNumberExpression(String line, Map<String, Variable> variables, List<Function> functions) {
        String special = "()^*/+-";
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
