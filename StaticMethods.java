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

    // takes an List of Strings for each line of code and index of line where for loop begins
    public static void forLoop(List<String> code, int start) {
        int indent = 0;
        int temp = 0;
        Iterator<String> itr = code.listIterator(start);
        // checks indentation level of line containing forLoop call
        for (char c : itr.next().toCharArray()) {
            if (c == ' ') {
                temp++;
            }
            else {
                break;
            }
        }
        // checks that there is a next line with indentation greater than forLoop call
        if (itr.hasNext()) {
            for (char c : itr.next().toCharArray()) {
                if (c == ' ') {
                    indent++;
                }
                else {
                    break;
                }
            }
        }
        if (temp < indent) {
                while (itr.hasNext()) {
                    String line = itr.next();
                    // TODO: finish this lol
                }
            }
        else {
            // TODO: error logging for incorrect indentation
        }
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
                if (c == Keywords.OPEN_PAREN_KEYWORD) {
                    tokenType = TokenType.Expression;
                }
                else if (c == Keywords.STRING_LITERAL_KEYWORD) {
                    tokenType = TokenType.String;
                }
                else {

                }
            }
            else if (tokenType == TokenType.Expression || tokenType == TokenType.StringInExpression) {
                if (c == Keywords.STRING_LITERAL_KEYWORD) {
                    if (tokenType == TokenType.Expression) {
                        tokenType = TokenType.StringInExpression;
                    }
                    else if (tokenType == TokenType.StringInExpression) {
                        tokenType = TokenType.StringInExpression;
                    }
                }
                if (tokenType != TokenType.StringInExpression && (c == Keywords.OPEN_PAREN_KEYWORD || c == Keywords.CLOSE_PAREN_KEYWORD)) {
                    if
                }
                currentToken += c;
            }
            
        }
        return result;
    }

    //TODO: WRITE THIS METHOD
    //1+2*4/6^2-(6+2)
    public double interpretNumberExpression(String line, Map<String, Variable> variables) {
        String special = "+-*/^()";
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
