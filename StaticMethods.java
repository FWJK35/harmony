import java.util.*;

public class StaticMethods {
    private enum TokenType {
        None, Variable, Integer, Double, Character, String, Array
    };

    public static void print(String line, Map<String, Variable> variables) {
        // get beginning of string literal and trim line
        int beginIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD);
        int endIndex = line.indexOf(Keywords.STRING_LITERAL_KEYWORD, beginIndex + 1);
        String remaining = line.substring(endIndex + Keywords.STRING_LITERAL_KEYWORD.length());
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
    private static Variable interpretExpression(String line, Map<String, Variable> variables) {
        Variable result = new Variable();
        List<String> tokens = new ArrayList<String>();
        String currentToken = "";
        TokenType tokenType = TokenType.None;
        boolean gettingKeyword = false;
        int currentCharIndex = 0;
        for (char c : line.toCharArray()) {
            
            //check for string start/end
            
            //check for beginning of keyword
            if (c == Keywords.STRING_LITERAL_KEYWORD.charAt(0)) {
                gettingKeyword = true;
                currentCharIndex++;
            }
            //check for end of keyword
            else if (c == Keywords.STRING_LITERAL_KEYWORD.charAt(currentCharIndex)) {
                if (currentCharIndex == Keywords.STRING_LITERAL_KEYWORD.length() - 1) {
                    currentCharIndex = 0;
                    if (tokenType == TokenType.None) {
                        //begin string token
                        tokenType = TokenType.String;
                    }
                    else if (tokenType == TokenType.String) {
                        //finish string token
                        tokenType = TokenType.None;
                        tokens.add(currentToken);
                        currentToken = "";
                    }
                    else {
                        throw new Error("Incorrect syntax interpreting expression");
                    }
                    gettingKeyword = false;
                }
                else {
                    currentCharIndex++;
                }
            }
            //cancel keyword interpretation
            else {
                if (gettingKeyword) {
                    gettingKeyword = false;
                }
            }
            if (tokenType == TokenType.String) {
                currentToken += c;
            }
            
        }


        return result;
    }
}
