import java.util.*;

public class StaticMethods {
    private enum TokenType {
        None, Variable, Number, String, Array, Expression, StringInExpression
    };

    public static void print(String line, Map<String, Variable> variables) {
        // get beginning of string literal and trim line
        int beginIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD);
        int endIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD, beginIndex + 1);
        String remaining = line.substring(endIndex + 1);
        //System.out.println(beginIndex + " " + endIndex + remaining);
        if (remaining.length() > 0) {
            throw new Error("Invalid Arguments to command: " + Keywords.PRINT_KEYWORD);
        }
        else {
            System.out.println(interpretExpression(line, variables));
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

    //xd #My name is # name # and my age is # 69
    public static Variable interpretExpression(String line, Map<String, Variable> variables) {
        Variable result = new Variable();
        List<String> tokenValues = new ArrayList<String>();
        List<TokenType> tokenTypes = new ArrayList<TokenType>();
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
                                tokenType = TokenType.StringInExpression;
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
                    tokenType = TokenType.None;
                }
                //begin number expression
                else if (c >= 48 && c <= 57) {

                }
                else {

                }
            }
            
        }
        return result;
    }

    //TODO: WRITE THIS METHOD
    //1+2*4/6^2-(6+2)
    public double interpretNumberExpression(String line, Map<String, Variable> variables) {
        String special = "()^*/+-";
        int left = 0;
        int right = 0;
        for (char i : line.toCharArray()) {
            right++;
            if(special.contains(i + "")) {
                if (i=='+') {
                    return Integer.parseInt(line.substring(left,right)) * interpretNumberExpression(line.substring(right), variables);
                }
                else if (i=='(') {
                }
            }
        }
        return 0.0;
        
    }
}
